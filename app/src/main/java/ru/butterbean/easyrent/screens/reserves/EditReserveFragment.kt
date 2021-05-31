package ru.butterbean.easyrent.screens.reserves

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_edit_reserve.*
import ru.butterbean.easyrent.CURRENT_RESERVE
import ru.butterbean.easyrent.CURRENT_ROOM
import ru.butterbean.easyrent.R
import ru.butterbean.easyrent.database.view_models.ReserveViewModel
import ru.butterbean.easyrent.models.ReserveData
import ru.butterbean.easyrent.screens.base.BaseFragment
import ru.butterbean.easyrent.utils.APP_ACTIVITY
import ru.butterbean.easyrent.utils.showToast
import java.util.*

class EditReserveFragment() : BaseFragment(R.layout.fragment_edit_reserve) {

    private var mIsNew = false
    private lateinit var mReserveViewModel: ReserveViewModel
    private lateinit var mDateCheckInSetListener:DatePickerDialog.OnDateSetListener
    private lateinit var mDateCheckOutSetListener:DatePickerDialog.OnDateSetListener
    private lateinit var mTimeCheckInSetListener:TimePickerDialog.OnTimeSetListener
    private lateinit var mTimeCheckOutSetListener:TimePickerDialog.OnTimeSetListener

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.confirm_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.confirm_change -> {
                change()
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

        if (guest.isEmpty()) {
            showToast("Введите имя гостя!")
        } else {
            val reserve = ReserveData(
                CURRENT_RESERVE.id,
                CURRENT_ROOM.id,
                1,
                guestsCount,
                sum,
                payment,
                dateCheckIn,
                dateCheckOut,
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

    override fun onResume() {
        super.onResume()

        mReserveViewModel = ViewModelProvider(this).get(ReserveViewModel::class.java)
        mIsNew = CURRENT_RESERVE.roomId == 0

        APP_ACTIVITY.title = getString(R.string.reserve)

        edit_reserve_room_name.text = CURRENT_RESERVE.roomId.toString()
        edit_reserve_guest.setText(CURRENT_RESERVE.guestId.toString())

        edit_reserve_date_check_in.setOnClickListener {showCalendarDialogFromListener(mDateCheckInSetListener)}
        mDateCheckInSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            edit_reserve_date_check_in.text = getDateString(month, dayOfMonth, year)
        }

        edit_reserve_time_check_in.setOnClickListener {showTimeDialogFromListener(mTimeCheckInSetListener)}
        mTimeCheckInSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            edit_reserve_time_check_in.text = getTimeString(hourOfDay, minute)
        }

        edit_reserve_date_check_out.setOnClickListener {showCalendarDialogFromListener(mDateCheckOutSetListener)}
        mDateCheckOutSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            edit_reserve_date_check_out.text = getDateString(month, dayOfMonth, year)
        }

        edit_reserve_time_check_out.setOnClickListener {showTimeDialogFromListener(mTimeCheckOutSetListener)}
        mTimeCheckOutSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            edit_reserve_time_check_out.text = getTimeString(hourOfDay, minute)
        }

        if (mIsNew) {
            edit_reserve_guests_count.setText("1")
        } else {
            edit_reserve_guests_count.setText(CURRENT_RESERVE.guestsCount)
        }

        // add menu
        setHasOptionsMenu(true)
    }

    private fun getDateString(month: Int, dayOfMonth: Int, year: Int): String {
        val monthText = (month + 1).toString().padStart(2, '0')
        val dayText = dayOfMonth.toString().padStart(2, '0')
        return "$dayText-$monthText-$year"
    }

    private fun getTimeString(hour: Int, minute: Int): String {
        val hourText = hour.toString().padStart(2, '0')
        val minuteText = minute.toString().padStart(2, '0')
        return "$hourText:$minuteText"
    }

    private fun showCalendarDialogFromListener(listener:DatePickerDialog.OnDateSetListener) {
        val cal = Calendar.getInstance()
        val dialog = DatePickerDialog(
            requireContext(),
            android.R.style.Theme_Material_Light_Dialog_MinWidth,
            listener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        )
        dialog.show()
    }

    private fun showTimeDialogFromListener(listener:TimePickerDialog.OnTimeSetListener) {
        val dialog = TimePickerDialog(
            requireContext(),
            android.R.style.Theme_Material_Light_Dialog,
            listener,
            12,
            0,
            true
        )
        dialog.show()
    }


}