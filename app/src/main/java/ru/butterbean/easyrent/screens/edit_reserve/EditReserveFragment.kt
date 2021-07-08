package ru.butterbean.easyrent.screens.edit_reserve

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.*
import android.net.Uri
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.text.InputFilter
import android.view.*
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import ru.butterbean.easyrent.R
import ru.butterbean.easyrent.databinding.FragmentEditReserveBinding
import ru.butterbean.easyrent.models.*
import ru.butterbean.easyrent.screens.ext_files.ExtFilesExtensionFragment
import ru.butterbean.easyrent.utils.*
import java.io.*
import java.util.*

class EditReserveFragment : ExtFilesExtensionFragment() {

    private var mIsNew = false
    private var mImmediatelyReplaceToArchive = false
    private var _binding: FragmentEditReserveBinding? = null
    private val mBinding get() = _binding!!
    private lateinit var mViewModel: EditReserveViewModel
    private lateinit var mCurrentReserve: ReserveData
    private lateinit var mCurrentRoom: RoomData
    private lateinit var mDateCheckInSetListener: DatePickerDialog.OnDateSetListener
    private lateinit var mDateCheckOutSetListener: DatePickerDialog.OnDateSetListener
    private lateinit var mTimeCheckInSetListener: TimePickerDialog.OnTimeSetListener
    private lateinit var mTimeCheckOutSetListener: TimePickerDialog.OnTimeSetListener
    private var mCurrentDateCheckIn = "" // date in format yyyy-MM-dd
    private var mCurrentDateCheckOut = "" // date in format yyyy-MM-dd
    private val mListCurrentExtFiles = mutableListOf<EmptyExtFileData>()

    private class EmptyExtFileData(
        val dirName: String,
        val fileName: String,
        val fileType: String,
        val isImage: Boolean
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentEditReserveBinding.inflate(layoutInflater, container, false)
        mCurrentReserve = arguments?.getSerializable("reserve") as ReserveData
        return mBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        // удалим временные внешние файлы, список не пустой только когда отказались от записи
        if (mListCurrentExtFiles.count() > 0) {
            try {
                mListCurrentExtFiles.forEach {
                    deleteLocalFile(it.dirName)
                }
                mListCurrentExtFiles.clear()
            } catch (e: Exception) {
                showToast(e.message.toString())
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (mIsNew) inflater.inflate(R.menu.confirm_menu, menu)
        else inflater.inflate(R.menu.confirm_delete_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> APP_ACTIVITY.navController.popBackStack()
            R.id.confirm_change -> {
                change()
                true
            }
            R.id.delete -> {
                showDeleteDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        extOnActivityResult(resultCode, requestCode, data, this)
    }

    override fun addExtFileToDatabase(dirName: String, fileName: String, extension: String) {
        if (mIsNew) {
            mListCurrentExtFiles.add(
                EmptyExtFileData(
                    dirName,
                    fileName,
                    extension,
                    extension.isImageExtension()
                )
            )
        } else {
            mViewModel.addReserveExtFile(
                ReserveExtFileData(
                    0,
                    mCurrentReserve.id,
                    dirName,
                    fileName,
                    extension,
                    extension.isImageExtension()
                )
            ) { extFilesCount ->
                mCurrentReserve.extFilesCount = extFilesCount
                mViewModel.updateReserve(mCurrentReserve) {
                    changeExtFilesButtonsVisibility()
                }
            }
        }
        changeExtFilesButtonsVisibility()
    }

    private fun showDeleteDialog() {
        val builder = AlertDialog.Builder(this.context)
        builder.setMessage(getString(R.string.finally_reserve_delete_message))
            .setPositiveButton(APP_ACTIVITY.getString(R.string.yes)) { dialog, _ ->
                dialog.cancel()
                mViewModel.deleteReserve(mCurrentReserve) {
                    goToRoomFragment()
                }
            }
            .setNegativeButton(APP_ACTIVITY.getString(R.string.no)) { dialog, _ ->
                dialog.cancel()
            }
            .show()
    }

    private fun change() {
        hideKeyboard()
        val guest = mBinding.editReserveGuest.text.toString().trim()
        val guestsCount = mBinding.editReserveGuestsCount.text.toString().trim()
        val phoneNumber = mBinding.editReservePhoneNumber.text.toString().trim()
        val sum = mBinding.editReserveSum.text.toString().trim()
        val payment = mBinding.editReservePayment.text.toString().trim()
        val wasCheckIn = mBinding.editReserveWasCheckIn.isChecked
        val wasCheckOut = mBinding.editReserveWasCheckOut.isChecked
        val dateCheckIn = mBinding.editReserveDateCheckIn.text.toString()
        val dateCheckOut = mBinding.editReserveDateCheckOut.text.toString()
        val timeCheckIn = mBinding.editReserveTimeCheckIn.text.toString()
        val timeCheckOut = mBinding.editReserveTimeCheckOut.text.toString()

        val dateCheckInText = getDateTimeInDatabaseFormat(mCurrentDateCheckIn, timeCheckIn)
        val dateCheckOutText = getDateTimeInDatabaseFormat(mCurrentDateCheckOut, timeCheckOut)

        when {
            dateCheckIn.isEmpty() -> showToast(getString(R.string.enter_checkin_date))
            timeCheckIn.isEmpty() -> showToast(getString(R.string.enter_checkin_time))
            dateCheckOut.isEmpty() -> showToast(getString(R.string.enter_checkout_date))
            timeCheckOut.isEmpty() -> showToast(getString(R.string.enter_checkout_time))
            getCalendarFromString(dateCheckInText).after(getCalendarFromString(dateCheckOutText)) -> showToast(
                getString(R.string.checkin_after_checkout)
            )
            guest.isEmpty() -> showToast(getString(R.string.enter_guest_name))
            guestsCount.isEmpty() -> showToast(getString(R.string.enter_guests_count))
            else -> {
                val sumInt = if (sum.isEmpty()) 0 else Integer.parseInt(sum)
                val paymentInt = if (payment.isEmpty()) 0 else Integer.parseInt(payment)
                if (mImmediatelyReplaceToArchive && reserveCompleted(
                        wasCheckOut,
                        sumInt,
                        paymentInt
                    )
                ) {
                    val reserve = ReserveArchiveData(
                        0,
                        mCurrentReserve.roomId,
                        guest,
                        Integer.parseInt(guestsCount),
                        sumInt,
                        paymentInt,
                        dateCheckInText,
                        dateCheckOutText,
                        wasCheckIn || wasCheckOut,
                        wasCheckOut,
                        phoneNumber,
                        if (mIsNew) mListCurrentExtFiles.count() else mCurrentReserve.extFilesCount
                    )

                    if (mIsNew) {
                        // если новое бронирование - добавляем сразу в архив
                        mViewModel.addReserveArchive(reserve) { newId ->
                            // прикрепим новые файлы
                            val listExtFiles = mutableListOf<ReserveArchiveExtFileData>()
                            mListCurrentExtFiles.forEach {
                                listExtFiles.add(
                                    ReserveArchiveExtFileData(
                                        0,
                                        newId,
                                        it.dirName,
                                        it.fileName,
                                        it.fileType,
                                        it.isImage
                                    )
                                )
                            }
                            mViewModel.addReserveArchiveExtFiles(listExtFiles)
                            mListCurrentExtFiles.clear()
                            goToRoomFragment()
                        }
                    } else {
                        // если редактируем - удаляем из основной таблицы и записываем с изменениями сразу в архив
                        mViewModel.replaceReserveToArchive(
                            reserve,
                            mCurrentReserve
                        ) { goToRoomFragment() }
                    }
                } else {

                    val reserve = ReserveData(
                        mCurrentReserve.id,
                        mCurrentReserve.roomId,
                        guest,
                        Integer.parseInt(guestsCount),
                        if (sum.isEmpty()) 0 else Integer.parseInt(sum),
                        if (payment.isEmpty()) 0 else Integer.parseInt(payment),
                        dateCheckInText,
                        dateCheckOutText,
                        wasCheckIn || wasCheckOut,
                        wasCheckOut,
                        phoneNumber,
                        if (mIsNew) mListCurrentExtFiles.count() else mCurrentReserve.extFilesCount
                    )
                    if (mIsNew) {
                        // если новое бронирование - добавляем в базу
                        mViewModel.addReserve(reserve) { newId ->
                            val listExtFiles = mutableListOf<ReserveExtFileData>()
                            mListCurrentExtFiles.forEach {
                                listExtFiles.add(
                                    ReserveExtFileData(
                                        0,
                                        newId,
                                        it.dirName,
                                        it.fileName,
                                        it.fileType,
                                        it.isImage
                                    )
                                )
                            }
                            mViewModel.addReserveExtFiles(listExtFiles)
                            mListCurrentExtFiles.clear()
                            goToRoomFragment()
                        }
                    } else {
                        // если редактируем - записываем изменения
                        mViewModel.updateReserve(reserve) { goToRoomFragment() }
                    }
                }
            }
        }
    }

    private fun goToRoomFragment() {
        val action = if (ONLY_ONE_ROOM) R.id.action_editReserveFragment_to_roomFragment_as_main
        else R.id.action_editReserveFragment_to_roomFragment
        APP_ACTIVITY.navController.navigate(
            action,
            createArgsBundle("room", mCurrentRoom)
        )
    }

    override fun onStart() {
        super.onStart()
        initialize()
    }

    private fun initialize() {
        APP_ACTIVITY.title = "${getString(R.string.archive)}. ${getString(R.string.reserve)}"
        APP_ACTIVITY.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val prefs = PreferenceManager.getDefaultSharedPreferences(APP_ACTIVITY)
        val prefAnalyseDepth =
            prefs.getString("oldReservesAnalyseDepth", DAYS_TO_REPLACE_TO_ARCHIVE.toString())!!
        mImmediatelyReplaceToArchive =
            prefAnalyseDepth == "0" // если в настройках выбрано 0 дней до перемещения в резерв, то будем перемещать сразу же при сохранении изменений

        mViewModel = ViewModelProvider(APP_ACTIVITY).get(EditReserveViewModel::class.java)
        mIsNew = mCurrentReserve.id == 0.toLong()

        APP_ACTIVITY.title = getString(R.string.reserve)


        // получим модель помещения и его название из БД
        mViewModel.getRoomById(mCurrentReserve.roomId) { room ->
            mCurrentRoom = room
            mBinding.editReserveRoomName.text = room.name
        }

        changeExtFilesButtonsVisibility()

        mBinding.editReserveSum.setText(if (mCurrentReserve.sum == 0) "" else mCurrentReserve.sum.toString())
        mBinding.editReservePayment.setText(if (mCurrentReserve.payment == 0) "" else mCurrentReserve.payment.toString())

        mBinding.editReserveGuest.setText(mCurrentReserve.guestName)
        mBinding.editReservePhoneNumber.hint = "+${getCountryZipCode()} XXX XXX-XX-XX"
        mBinding.editReservePhoneNumber.setText(mCurrentReserve.phoneNumber)
        mBinding.editReserveWasCheckIn.isChecked = mCurrentReserve.wasCheckIn
        mBinding.editReserveWasCheckOut.isChecked = mCurrentReserve.wasCheckOut
        if (mCurrentReserve.dateCheckIn.isNotEmpty()) {
            mCurrentDateCheckIn = mCurrentReserve.dateCheckIn.substring(0, 10)
            mBinding.editReserveDateCheckIn.text = mCurrentReserve.dateCheckIn.toDateFormat(false)
            mBinding.editReserveTimeCheckIn.text = mCurrentReserve.dateCheckIn.toTimeFormat()
            changeCheckInGroupColor(mBinding.editReserveWasCheckIn.isChecked)
        }
        if (mCurrentReserve.dateCheckOut.isNotEmpty()) {
            mCurrentDateCheckOut = mCurrentReserve.dateCheckOut.substring(0, 10)
            mBinding.editReserveDateCheckOut.text = mCurrentReserve.dateCheckOut.toDateFormat(false)
            mBinding.editReserveTimeCheckOut.text = mCurrentReserve.dateCheckOut.toTimeFormat()
            changeCheckOutGroupColor(mBinding.editReserveWasCheckOut.isChecked)
        }
        // guest count
        if (mIsNew) {
            mBinding.editReserveGuestsCount.setText("1")
        } else {
            mBinding.editReserveGuestsCount.setText(mCurrentReserve.guestsCount.toString())
        }

        mBinding.editReserveBtnPhoneCall.setOnClickListener {
            val intent =
                Intent(
                    Intent.ACTION_DIAL,
                    Uri.parse("tel:" + mBinding.editReservePhoneNumber.text.toString())
                )
            startActivity(intent)
        }

        mBinding.editReserveBtnPhoneWhatsapp.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data =
                Uri.parse("https://api.whatsapp.com/send?phone=" + mBinding.editReservePhoneNumber.text.toString())
            startActivity(intent)
        }

        mBinding.editReserveBtnPhoneSms.setOnClickListener {
            val intent =
                Intent(
                    Intent.ACTION_SENDTO,
                    Uri.parse("smsto:" + mBinding.editReservePhoneNumber.text.toString())
                )
            startActivity(intent)
        }

        changePhoneButtonsInEnabled()

        setViewsSettings()

        // добавляем меню
        setHasOptionsMenu(true)
    }

    private fun changeExtFilesButtonsVisibility() {
        if (mIsNew) setExtFilesButtonsVisibility(mListCurrentExtFiles.count())
        else mViewModel.getExtFilesCount(mCurrentReserve.id) { setExtFilesButtonsVisibility(it) }
    }

    private fun setExtFilesButtonsVisibility(filesCount: Int) {
        when (filesCount) {
            0 -> {
                mBinding.editReserveBtnShowFiles.visibility = View.GONE
                mBinding.editReserveBtnShowSingleFile.visibility = View.GONE
                mBinding.editReserveBtnAddFileAttachImage.visibility = View.VISIBLE
            }
            1 -> {
                mBinding.editReserveBtnShowFiles.visibility = View.GONE
                mBinding.editReserveBtnShowSingleFile.visibility = View.VISIBLE
                mBinding.editReserveBtnShowSingleFile.setSingleExtFileImage(this)
                mBinding.editReserveBtnShowSingleFile.isEnabled = !mIsNew
                mBinding.editReserveBtnAddFileAttachImage.visibility = View.VISIBLE
            }
            else -> {
                mBinding.editReserveBtnShowFiles.visibility = View.VISIBLE
                mBinding.editReserveBtnShowSingleFile.visibility = View.GONE
                mBinding.editReserveBtnShowFilesText.text = filesCount.toString()
                mBinding.editReserveBtnShowFiles.isEnabled = !mIsNew
                mBinding.editReserveBtnAddFileAttachImage.visibility = View.GONE
            }
        }
    }

    override fun getSingleExtFileParams(f: (Bundle) -> Unit) {

        if (mIsNew) {
            f(
                getSingleExtFileParamsBundle(
                    mListCurrentExtFiles[0].dirName,
                    mListCurrentExtFiles[0].fileName,
                    mListCurrentExtFiles[0].fileType,
                    mListCurrentExtFiles[0].isImage
                )
            )
        } else {
            mViewModel.getSingleExtFileByReserveId(mCurrentReserve.id) { extFile ->
                f(extFile.getParamsBundle())
            }
        }
    }

    private fun changePhoneButtonsInEnabled() {
        val vis = !phoneNumberIsEmpty(mBinding.editReservePhoneNumber.text.toString())
        mBinding.editReserveBtnPhoneCall.isVisible = vis
        mBinding.editReserveBtnPhoneWhatsapp.isVisible = vis
        mBinding.editReserveBtnPhoneSms.isVisible = vis
    }

    private inner class MyPhoneNumberFormattingTextWatcher : PhoneNumberFormattingTextWatcher() {
        override fun afterTextChanged(s: Editable?) {
            super.afterTextChanged(s)
            this@EditReserveFragment.changePhoneButtonsInEnabled()
        }
    }

    private fun setViewsSettings() {
        changePaymentBtnVisibility()
        changeWasCheckInEnabled()
        changeWasCheckOutEnabled()

        // phone

        mBinding.editReservePhoneNumber.addTextChangedListener(MyPhoneNumberFormattingTextWatcher())

        // date check-in диалог
        mBinding.editReserveDateCheckIn.setOnClickListener {
            hideKeyboard()
            showCalendarDialogFromListener(
                requireContext(),
                mDateCheckInSetListener,
                mCurrentDateCheckIn
            )
        }
        mDateCheckInSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            mCurrentDateCheckIn = getDateFormatISO(year, month, dayOfMonth)
            mBinding.editReserveDateCheckIn.text = mCurrentDateCheckIn.toDateFormat(true)
            changeCheckInGroupColor(mBinding.editReserveWasCheckIn.isChecked)
            changeWasCheckInEnabled()
        }

        // time check-in диалог
        mBinding.editReserveTimeCheckIn.setOnClickListener {
            showTimeDialogFromListener(
                requireContext(),
                mTimeCheckInSetListener,
                mBinding.editReserveTimeCheckIn.text.toString()
            )
        }
        mTimeCheckInSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            mBinding.editReserveTimeCheckIn.text = getTimeString(hourOfDay, minute)
        }

        // date check-out диалог
        mBinding.editReserveDateCheckOut.setOnClickListener {
            showCalendarDialogFromListener(
                requireContext(),
                mDateCheckOutSetListener,
                mCurrentDateCheckOut,
                mCurrentDateCheckIn
            )
        }
        mDateCheckOutSetListener =
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                mCurrentDateCheckOut = getDateFormatISO(year, month, dayOfMonth)
                mBinding.editReserveDateCheckOut.text = mCurrentDateCheckOut.toDateFormat(true)
                changeCheckOutGroupColor(mBinding.editReserveWasCheckOut.isChecked)
                changeWasCheckOutEnabled()
            }

        // time check-out диалог
        mBinding.editReserveTimeCheckOut.setOnClickListener {
            showTimeDialogFromListener(
                requireContext(),
                mTimeCheckOutSetListener,
                mBinding.editReserveTimeCheckOut.text.toString()
            )
        }
        mTimeCheckOutSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            mBinding.editReserveTimeCheckOut.text = getTimeString(hourOfDay, minute)
        }

        mBinding.editReserveGuestsCount.filters = arrayOf<InputFilter>(NumberEditTextInputFilter())

        mBinding.editReserveWasCheckIn.setOnCheckedChangeListener { _, isChecked ->
            changeCheckInGroupColor(isChecked)
            changeWasCheckOutEnabled()
        }

        mBinding.editReserveWasCheckOut.setOnCheckedChangeListener { _, isChecked ->
            changeCheckOutGroupColor(isChecked)
            changeWasCheckInEnabled()
        }

        // суммы
        mBinding.editReserveSum.addTextChangedListener {
            changePaymentBtnVisibility()
        }

        mBinding.editReservePayment.addTextChangedListener {
            changePaymentBtnVisibility()
        }

        mBinding.editReserveBtnPaymentFull.setOnClickListener {
            mBinding.editReservePayment.setText(mBinding.editReserveSum.text.toString())
            changePaymentBtnVisibility()
            hideKeyboard()
        }
        mBinding.editReserveBtnAddFile.setOnClickListener {
            showAttachFileDialog(this)
        }
        mBinding.editReserveBtnShowSingleFile.setOnClickListener {
            getSingleExtFileParams { extFileParams ->
                startAnyApp(extFileParams)
            }
        }
        mBinding.editReserveBtnShowSingleFile.setOnLongClickListener {
            getSingleExtFileParams {
                showDeleteExtFileDialog(it.getSerializable("extFile") as ReserveExtFileData, this)
            }
            true
        }
        mBinding.editReserveBtnShowFiles.setOnClickListener {
            APP_ACTIVITY.navController.navigate(
                R.id.action_editReserveFragment_to_extFilesListFragment,
                createArgsBundle("reserve", mCurrentReserve)
            )
        }
    }

    private fun changeWasCheckInEnabled() {
        mBinding.editReserveWasCheckIn.isEnabled =
            mCurrentDateCheckIn.isNotEmpty() && !mBinding.editReserveWasCheckOut.isChecked
    }

    private fun changeWasCheckOutEnabled() {
        mBinding.editReserveWasCheckOut.isEnabled =
            mCurrentDateCheckOut.isNotEmpty() && mBinding.editReserveWasCheckIn.isChecked
    }

    private fun changeCheckOutGroupColor(isChecked: Boolean) {
        changeCheckGroupColor(
            mCurrentDateCheckOut,
            isChecked,
            mBinding.editReserveGroupChekOut
        )
    }

    private fun changeCheckInGroupColor(isChecked: Boolean) {
        changeCheckGroupColor(
            mCurrentDateCheckIn,
            isChecked,
            mBinding.editReserveGroupChekIn
        )
    }

    private fun changeCheckGroupColor(date: String, isChecked: Boolean, groupView: View) {
        val dateCal = getCalendarFromString(getDateTimeInDatabaseFormat(date, "00:00"))
        val today = getStartOfDay(Calendar.getInstance())
        if (today.after(dateCal) && !isChecked) groupView.setBackgroundColor(APP_ACTIVITY.getColor(R.color.pink))
        else groupView.setBackgroundColor(APP_ACTIVITY.getColor(R.color.white))
    }

    private fun changePaymentBtnVisibility() {
        if (mBinding.editReserveSum.text.isEmpty() || mBinding.editReservePayment.text.toString() == mBinding.editReserveSum.text.toString()) {
            mBinding.editReserveBtnPaymentFull.visibility = View.INVISIBLE
        } else {
            mBinding.editReserveBtnPaymentFull.visibility = View.VISIBLE
        }
    }

    override fun deleteReserveExtFile(extFile: ReserveExtFileData) {
        mViewModel.deleteExtFile(extFile) { extFilesCount ->
            mCurrentReserve.extFilesCount = extFilesCount
            mViewModel.updateReserve(mCurrentReserve) {
                changeExtFilesButtonsVisibility()
            }
        }
    }
}