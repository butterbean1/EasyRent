package ru.butterbean.easyrent.utils

import android.app.AlertDialog
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import ru.butterbean.easyrent.R
import ru.butterbean.easyrent.database.view_models.ReserveViewModel
import ru.butterbean.easyrent.database.view_models.RoomViewModel
import ru.butterbean.easyrent.database.models.GuestData
import ru.butterbean.easyrent.database.models.ReserveData
import ru.butterbean.easyrent.database.models.RoomData
import ru.butterbean.easyrent.screens.reserves.EditReserveFragment
import ru.butterbean.easyrent.screens.room.EditRoomFragment
import java.util.*

fun getEmptyRoom(): RoomData {
    return RoomData(0)
}

fun getEmptyReserve(roomId: Long): ReserveData {
    return ReserveData(0, roomId)
}

fun getEmptyGuest(): GuestData {
    return GuestData(0)
}

fun showEditDeleteReserveDialog(reserve: ReserveData) {
    val actions = arrayOf(
        APP_ACTIVITY.getString(R.string.edit), // 0
        APP_ACTIVITY.getString(R.string.delete) // 1
    )
    val builder = AlertDialog.Builder(APP_ACTIVITY)
    builder.setItems(actions) { _, i ->
        when (i) {
            0 -> replaceFragment(EditReserveFragment())
            1 -> ReserveViewModel(APP_ACTIVITY.application).deleteReserve(reserve)
        }
    }
        .show()
}

fun showEditDeleteRoomDialog(room: RoomData, lo: LifecycleOwner) {
    val actions = arrayOf(
        APP_ACTIVITY.getString(R.string.edit), // 0
        APP_ACTIVITY.getString(R.string.delete) // 1
    )
    val builder = AlertDialog.Builder(APP_ACTIVITY)
    builder.setItems(actions) { _, i ->
        when (i) {
            0 -> replaceFragment(EditRoomFragment())
            1 -> deleteRoomWithDialog(room, lo)
        }
    }
        .show()
}

fun deleteReserveWithDialog(reserve: ReserveData) {
    val builder = AlertDialog.Builder(APP_ACTIVITY)
    builder.setMessage(APP_ACTIVITY.getString(R.string.question_delete_reserve))
        .setPositiveButton(APP_ACTIVITY.getString(R.string.yes)) { dialog, _ ->
            ReserveViewModel(APP_ACTIVITY.application).deleteReserve(reserve)
            dialog.cancel()
            APP_ACTIVITY.supportFragmentManager.popBackStack()
        }
        .setNegativeButton(APP_ACTIVITY.getString(R.string.no)) { dialog, _ ->
            dialog.cancel()
        }
        .show()
}

fun deleteRoomWithDialog(room: RoomData, lo: LifecycleOwner) {
    val roomViewModel = RoomViewModel(APP_ACTIVITY.application)
    // если нет бронирований, то не будем ничего спрашивать
    roomViewModel.getReservesCount(room.id).observe(lo, { count ->
        if (count == 0) {
            roomViewModel.deleteRoom(room)
            APP_ACTIVITY.supportFragmentManager.popBackStack()
        } else {
            val builder = AlertDialog.Builder(APP_ACTIVITY)
            builder.setMessage(APP_ACTIVITY.getString(R.string.question_delete_room))
                .setPositiveButton(APP_ACTIVITY.getString(R.string.yes)) { dialog, _ ->
                    roomViewModel.deleteRoom(room)
                    dialog.cancel()
                    APP_ACTIVITY.supportFragmentManager.popBackStack()
                }
                .setNegativeButton(APP_ACTIVITY.getString(R.string.no)) { dialog, _ ->
                    dialog.cancel()
                }
                .show()
        }
    })
}


