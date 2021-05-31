package ru.butterbean.easyrent.screens.reserves

import android.view.*
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_edit_reserve.*
import ru.butterbean.easyrent.CURRENT_RESERVE
import ru.butterbean.easyrent.CURRENT_ROOM
import ru.butterbean.easyrent.R
import ru.butterbean.easyrent.STATUS_FREE
import ru.butterbean.easyrent.database.view_models.ReserveViewModel
import ru.butterbean.easyrent.models.ReserveData
import ru.butterbean.easyrent.models.RoomData
import ru.butterbean.easyrent.screens.base.BaseFragment
import ru.butterbean.easyrent.utils.APP_ACTIVITY
import ru.butterbean.easyrent.utils.showToast

class EditReserveFragment() : BaseFragment(R.layout.fragment_edit_reserve) {

    private var mIsNew = false
    private lateinit var mReserveViewModel: ReserveViewModel

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
                CURRENT_RESERVE.roomId,
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

        mReserveViewModel = ViewModelProvider(this).get(mReserveViewModel::class.java)
        mIsNew = CURRENT_RESERVE.roomId == 0

        APP_ACTIVITY.title = getString(R.string.reserve)

        edit_reserve_room_name.setText(CURRENT_RESERVE.roomId)
        edit_reserve_guest.setText(CURRENT_RESERVE.guestId)
        if (mIsNew) {
            edit_reserve_guests_count.setText("1")
        } else {
            edit_reserve_guests_count.setText(CURRENT_RESERVE.guestsCount)
        }

        // add menu
        setHasOptionsMenu(true)
    }


}