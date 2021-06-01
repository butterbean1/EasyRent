package ru.butterbean.easyrent.screens.reserves

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_edit_reserve.*
import ru.butterbean.easyrent.R
import ru.butterbean.easyrent.database.CURRENT_RESERVE
import ru.butterbean.easyrent.database.CURRENT_ROOM
import ru.butterbean.easyrent.database.view_models.GuestViewModel
import ru.butterbean.easyrent.database.view_models.ReserveViewModel
import ru.butterbean.easyrent.models.ReserveData
import ru.butterbean.easyrent.screens.base.BaseFragment
import ru.butterbean.easyrent.utils.*

class EditReserveFragment() : BaseFragment(R.layout.fragment_edit_reserve) {

    private var mIsNew = false
    private lateinit var mReserveViewModel: ReserveViewModel
    private lateinit var mGuestViewModel: GuestViewModel
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
                deleteReserveWithDialog(CURRENT_RESERVE)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun change() {
        val guest = edit_reserve_guest.text.toString()
        val guestsCount = Integer.parseInt(edit_reserve_guests_count.text.toString())
        val sum = Integer.parseInt(edit_reserve_sum.text.toString())
        val payment = Integer.parseInt(edit_reserve_payment.text.toString())
        val wasCheckIn = edit_reserve_was_check_in.isChecked
        val wasCheckOut = edit_reserve_was_check_out.isChecked
        val dateCheckIn = edit_reserve_date_check_in.text.toString()
        val dateCheckOut = edit_reserve_date_check_out.text.toString()
        val timeCheckIn = edit_reserve_time_check_in.text.toString()
        val timeCheckOut = edit_reserve_time_check_out.text.toString()

        val dateCheckInText = "${mCurrentDateCheckIn}T${timeCheckIn}:00"
        val dateCheckOutText = "${mCurrentDateCheckOut}T${timeCheckOut}:00"

        when {
            dateCheckIn.isEmpty() -> showToast("Выберите дату заселения!")
            timeCheckIn.isEmpty() -> showToast("Выберите время заселения!")
            dateCheckOut.isEmpty() -> showToast("Выберите дату выселения!")
            timeCheckOut.isEmpty() -> showToast("Выберите время выселения!")
            guest.isEmpty() -> showToast("Введите имя гостя!")
            guestsCount == 0 -> showToast("Введите количество постояльцев!")
            else -> {
                val reserve = ReserveData(
                    CURRENT_RESERVE.id,
                    CURRENT_ROOM.id,
                    guest,
                    guestsCount,
                    sum,
                    payment,
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

        mReserveViewModel = ViewModelProvider(this).get(ReserveViewModel::class.java)
        mGuestViewModel = ViewModelProvider(this).get(GuestViewModel::class.java)
        mIsNew = CURRENT_RESERVE.roomId == 0

        APP_ACTIVITY.title = getString(R.string.reserve)

        edit_reserve_room_name.text = CURRENT_ROOM.name
        edit_reserve_guest.setText(CURRENT_RESERVE.guestName)
        edit_reserve_sum.setText(CURRENT_RESERVE.sum.toString())
        edit_reserve_payment.setText(CURRENT_RESERVE.payment.toString())
        edit_reserve_was_check_in.isChecked = CURRENT_RESERVE.wasCheckIn
        edit_reserve_was_check_out.isChecked = CURRENT_RESERVE.wasCheckOut
        if (!mIsNew) {
            mCurrentDateCheckIn = CURRENT_RESERVE.dateCheckIn.substring(0, 10)
            mCurrentDateCheckOut = CURRENT_RESERVE.dateCheckOut.substring(0, 10)
            edit_reserve_date_check_in.text = CURRENT_RESERVE.dateCheckIn.toDateFormat(false)
            edit_reserve_date_check_out.text = CURRENT_RESERVE.dateCheckOut.toDateFormat(false)
            edit_reserve_time_check_in.text = CURRENT_RESERVE.dateCheckIn.toTimeFormat()
            edit_reserve_time_check_out.text = CURRENT_RESERVE.dateCheckOut.toTimeFormat()
        }

        edit_reserve_date_check_in.setOnClickListener {
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
            edit_reserve_guests_count.setText(CURRENT_RESERVE.guestsCount.toString())
        }

        // add menu
        setHasOptionsMenu(true)
    }

}