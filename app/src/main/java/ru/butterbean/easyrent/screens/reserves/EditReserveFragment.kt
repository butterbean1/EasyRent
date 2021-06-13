package ru.butterbean.easyrent.screens.reserves

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.InputFilter
import android.text.Spanned
import android.view.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_edit_reserve.*
import ru.butterbean.easyrent.R
import ru.butterbean.easyrent.database.view_models.EditReserveViewModel
import ru.butterbean.easyrent.databinding.FragmentEditReserveBinding
import ru.butterbean.easyrent.models.ReserveData
import ru.butterbean.easyrent.models.RoomData
import ru.butterbean.easyrent.utils.*
import java.util.*

class EditReserveFragment : Fragment() {

    private var mIsNew = false
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
                mViewModel.deleteReserve(mCurrentReserve){goToRoomFragment()}
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun change() {
        hideKeyboard()
        val guest = mBinding.editReserveGuest.text.toString().trim()
        val guestsCount = mBinding.editReserveGuestsCount.text.toString().trim()
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
                    wasCheckOut
                )
                if (mIsNew) {
                    // если новое бронирование - добавляем в базу
                    mViewModel.addReserve(reserve) { goToRoomFragment() }
                } else {
                    // если редактируем - записываем изменения
                    mViewModel.updateReserve(reserve) { goToRoomFragment() }
                }
            }
        }
    }

    private fun goToRoomFragment() {
        APP_ACTIVITY.navController.navigate(
            R.id.action_editReserveFragment_to_roomFragment,
            createArgsBundle("room", mCurrentRoom)
        )
    }

    override fun onStart() {
        super.onStart()
        initialize()
    }

    private fun initialize() {
        APP_ACTIVITY.title = getString(R.string.reserve)
        APP_ACTIVITY.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mViewModel = ViewModelProvider(APP_ACTIVITY).get(EditReserveViewModel::class.java)
        mIsNew = mCurrentReserve.id == 0.toLong()

        // получим модель помещения и его название из БД
        mViewModel.getRoomById(mCurrentReserve.roomId).observe(viewLifecycleOwner, { room ->
            mCurrentRoom = room
            mBinding.editReserveRoomName.text = room.name
        })

        mBinding.editReserveGuest.setText(mCurrentReserve.guestName)
        mBinding.editReserveSum.setText(if (mCurrentReserve.sum == 0) "" else mCurrentReserve.sum.toString())
        mBinding.editReservePayment.setText(if (mCurrentReserve.payment == 0) "" else mCurrentReserve.payment.toString())
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
        changePaymentBtnVisibility()
        changeWasCheckInEnabled()
        changeWasCheckOutEnabled()

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

        // guest count
        if (mIsNew) {
            mBinding.editReserveGuestsCount.setText("1")
        } else {
            mBinding.editReserveGuestsCount.setText(mCurrentReserve.guestsCount.toString())
        }

        mBinding.editReserveGuestsCount.filters = arrayOf<InputFilter>(GuestsCountInputFilter())

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
            mBinding.editReservePayment.setText(edit_reserve_sum.text.toString())
            changePaymentBtnVisibility()
            hideKeyboard()
        }

        // добавляем меню
        setHasOptionsMenu(true)
    }

    private fun changeWasCheckInEnabled(){
        mBinding.editReserveWasCheckIn.isEnabled = mCurrentDateCheckIn.isNotEmpty() && !mBinding.editReserveWasCheckOut.isChecked
    }

    private fun changeWasCheckOutEnabled(){
        mBinding.editReserveWasCheckOut.isEnabled = mCurrentDateCheckOut.isNotEmpty() && mBinding.editReserveWasCheckIn.isChecked
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

    private fun changeCheckGroupColor(date: String, isCheked: Boolean, groupView: View) {
        val dateCal = getCalendarFromString(getDateTimeInDatabaseFormat(date, "00:00"))
        val today = getStartOfDay(Calendar.getInstance())
        if (today.after(dateCal) && !isCheked) groupView.setBackgroundColor(APP_ACTIVITY.getColor(R.color.pink))
        else groupView.setBackgroundColor(APP_ACTIVITY.getColor(R.color.white))
    }

    private fun changePaymentBtnVisibility() {
        if (mBinding.editReservePayment.text.toString() == mBinding.editReserveSum.text.toString()) {
            mBinding.editReserveBtnPaymentFull.visibility = View.INVISIBLE
        } else {
            mBinding.editReserveBtnPaymentFull.visibility = View.VISIBLE
        }

    }

    private class GuestsCountInputFilter : InputFilter {
        private val mMinValue: Int = 1 // минимум гостей для размещения
        private val mMaxValue: Int = 99 // максимум гостей для размещения

        override fun filter(
            source: CharSequence?,
            start: Int,
            end: Int,
            dest: Spanned?,
            dstart: Int,
            dend: Int
        ): CharSequence? {
            try {
                val input = (dest?.subSequence(0, dstart).toString() + source + dest?.subSequence(
                    dend,
                    dest.length
                )).toInt()
                if (isInRange(input))
                    return null
            } catch (e: NumberFormatException) {
            }
            return ""
        }

        private fun isInRange(c: Int): Boolean {
            return c in mMinValue..mMaxValue
        }
    }

}