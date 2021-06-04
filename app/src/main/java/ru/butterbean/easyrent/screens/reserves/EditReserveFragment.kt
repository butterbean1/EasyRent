package ru.butterbean.easyrent.screens.reserves

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.text.InputFilter
import android.text.Spanned
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_edit_reserve.*
import ru.butterbean.easyrent.R
import ru.butterbean.easyrent.database.view_models.ReserveViewModel
import ru.butterbean.easyrent.database.view_models.RoomViewModel
import ru.butterbean.easyrent.database.models.ReserveData
import ru.butterbean.easyrent.database.models.RoomData
import ru.butterbean.easyrent.screens.base.BaseFragment
import ru.butterbean.easyrent.utils.*
import java.lang.NumberFormatException

class EditReserveFragment() : BaseFragment(R.layout.fragment_edit_reserve) {

    private var mIsNew = false
    private lateinit var mReserveViewModel: ReserveViewModel
    private lateinit var mRoomViewModel: RoomViewModel
    private lateinit var mCurrentReserve: ReserveData
    private lateinit var mCurrentRoom: RoomData
    private lateinit var mDateCheckInSetListener: DatePickerDialog.OnDateSetListener
    private lateinit var mDateCheckOutSetListener: DatePickerDialog.OnDateSetListener
    private lateinit var mTimeCheckInSetListener: TimePickerDialog.OnTimeSetListener
    private lateinit var mTimeCheckOutSetListener: TimePickerDialog.OnTimeSetListener
    private var mCurrentDateCheckIn = "" // date in format yyyy-MM-dd
    private var mCurrentDateCheckOut = "" // date in format yyyy-MM-dd

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (mIsNew) inflater.inflate(R.menu.confirm_menu, menu)
        else inflater.inflate(R.menu.confirm_delete_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.confirm_change -> {
                change()
                true
            }
            R.id.delete -> {
                deleteReserveWithDialog(mCurrentReserve)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun change() {
        val guest = edit_reserve_guest.text.toString()
        val guestsCount = edit_reserve_guests_count.text.toString()
        val sum = edit_reserve_sum.text.toString()
        val payment = edit_reserve_payment.text.toString()
        val wasCheckIn = edit_reserve_was_check_in.isChecked
        val wasCheckOut = edit_reserve_was_check_out.isChecked
        val dateCheckIn = edit_reserve_date_check_in.text.toString()
        val dateCheckOut = edit_reserve_date_check_out.text.toString()
        val timeCheckIn = edit_reserve_time_check_in.text.toString()
        val timeCheckOut = edit_reserve_time_check_out.text.toString()

        val dateCheckInText = getDateTimeInDatabaseFormat(mCurrentDateCheckIn,timeCheckIn)
        val dateCheckOutText = getDateTimeInDatabaseFormat(mCurrentDateCheckOut,timeCheckOut)

        when {
            dateCheckIn.isEmpty() -> showToast("Выберите дату заселения!")
            timeCheckIn.isEmpty() -> showToast("Выберите время заселения!")
            dateCheckOut.isEmpty() -> showToast("Выберите дату выселения!")
            timeCheckOut.isEmpty() -> showToast("Выберите время выселения!")
            getCalendarFromString(dateCheckInText).after(getCalendarFromString(dateCheckOutText)) -> showToast(
                "Заселение позже выселения!"
            )
            guest.isEmpty() -> showToast("Введите имя гостя!")
            guestsCount.isEmpty() -> showToast("Введите количество постояльцев!")
            else -> {
                val reserve = ReserveData(
                    mCurrentReserve.id,
                    mCurrentReserve.roomId,
                    guest,
                    Integer.parseInt(guestsCount),
                    Integer.parseInt(sum),
                    Integer.parseInt(payment),
                    dateCheckInText,
                    dateCheckOutText,
                    wasCheckIn,
                    wasCheckOut
                )
                if (mIsNew) {
                    // если новое бронирование - добавляем в базу
                    mReserveViewModel.addReserve(reserve)
                } else {
                    // если редактируем - записываем изменения
                    mReserveViewModel.updateReserve(reserve)
                }

                APP_ACTIVITY.supportFragmentManager.popBackStack()

            }
        }
    }

    override fun onResume() {
        super.onResume()

        mReserveViewModel = ViewModelProvider(APP_ACTIVITY).get(ReserveViewModel::class.java)
        mCurrentReserve = mReserveViewModel.currentReserve
        mIsNew = mCurrentReserve.guestName.isEmpty()

        APP_ACTIVITY.title = getString(R.string.reserve)

        // получим название помещения из БД таблицы помещений
        mRoomViewModel = ViewModelProvider(APP_ACTIVITY).get(RoomViewModel::class.java)
        mRoomViewModel.getById(mCurrentReserve.roomId).observe(viewLifecycleOwner, { room ->
            mCurrentRoom = room
            edit_reserve_room_name.text = room.name
        })

        edit_reserve_guest.setText(mCurrentReserve.guestName)
        edit_reserve_sum.setText(mCurrentReserve.sum.toString())
        edit_reserve_payment.setText(mCurrentReserve.payment.toString())
        edit_reserve_was_check_in.isChecked = mCurrentReserve.wasCheckIn
        edit_reserve_was_check_out.isChecked = mCurrentReserve.wasCheckOut
        if (!mIsNew) {
            mCurrentDateCheckIn = mCurrentReserve.dateCheckIn.substring(0, 10)
            mCurrentDateCheckOut = mCurrentReserve.dateCheckOut.substring(0, 10)
            edit_reserve_date_check_in.text = mCurrentReserve.dateCheckIn.toDateFormat(false)
            edit_reserve_date_check_out.text = mCurrentReserve.dateCheckOut.toDateFormat(false)
            edit_reserve_time_check_in.text = mCurrentReserve.dateCheckIn.toTimeFormat()
            edit_reserve_time_check_out.text = mCurrentReserve.dateCheckOut.toTimeFormat()
        }
        changePaymentBtnVisibility()

        edit_reserve_date_check_in.setOnClickListener {
            hideKeyboard()
            showCalendarDialogFromListener(requireContext(), mDateCheckInSetListener)
        }
        mDateCheckInSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            mCurrentDateCheckIn = getDateFormatISO(year, month, dayOfMonth)
            edit_reserve_date_check_in.text = mCurrentDateCheckIn.toDateFormat(true)
        }

        edit_reserve_time_check_in.setOnClickListener {
            showTimeDialogFromListener(requireContext(), mTimeCheckInSetListener)
        }
        mTimeCheckInSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            edit_reserve_time_check_in.text = getTimeString(hourOfDay, minute)
        }

        edit_reserve_date_check_out.setOnClickListener {
            showCalendarDialogFromListener(requireContext(), mDateCheckOutSetListener)
        }
        mDateCheckOutSetListener =
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                mCurrentDateCheckOut = getDateFormatISO(year, month, dayOfMonth)
                edit_reserve_date_check_out.text = mCurrentDateCheckOut.toDateFormat(true)
            }

        edit_reserve_time_check_out.setOnClickListener {
            showTimeDialogFromListener(requireContext(), mTimeCheckOutSetListener)
        }
        mTimeCheckOutSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            edit_reserve_time_check_out.text = getTimeString(hourOfDay, minute)
        }

        if (mIsNew) {
            edit_reserve_guests_count.setText("1")
        } else {
            edit_reserve_guests_count.setText(mCurrentReserve.guestsCount.toString())
        }

        edit_reserve_guests_count.filters = arrayOf<InputFilter>(GuestsCountInputFilter());

        edit_reserve_sum.addTextChangedListener {
            changePaymentBtnVisibility()
        }

        edit_reserve_payment.addTextChangedListener {
            changePaymentBtnVisibility()
        }

        edit_reserve_btn_payment_full.setOnClickListener {
            edit_reserve_payment.setText(edit_reserve_sum.text.toString())
            changePaymentBtnVisibility()
        }

        // add menu
        setHasOptionsMenu(true)
    }

    private fun changePaymentBtnVisibility() {
        if (edit_reserve_payment.text.toString() == edit_reserve_sum.text.toString()) {
            edit_reserve_btn_payment_full.visibility = View.INVISIBLE
        } else {
            edit_reserve_btn_payment_full.visibility = View.VISIBLE
        }

    }

    private class GuestsCountInputFilter : InputFilter {
        private val mMinValue: Int = 1 // минимум гостей для размещения
        private val mMaxValue: Int = 20 // максимум гостей для размещения

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