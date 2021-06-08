package ru.butterbean.easyrent.utils

import android.app.AlertDialog
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import ru.butterbean.easyrent.R
import ru.butterbean.easyrent.database.view_models.EditReserveViewModel
import ru.butterbean.easyrent.database.view_models.RoomDialogViewModel
import ru.butterbean.easyrent.models.GuestData
import ru.butterbean.easyrent.models.ReserveData
import ru.butterbean.easyrent.models.RoomData

fun getEmptyRoom(): RoomData {
    return RoomData(0,"","", STATUS_FREE)
}

fun getEmptyReserve(roomId: Long): ReserveData {
    return ReserveData(0, roomId)
}

fun getEmptyGuest(): GuestData {
    return GuestData(0)
}

/* Пока что решил удалять без лишних вопросов
fun deleteReserveWithDialog(reserve: ReserveData) {
    val builder = AlertDialog.Builder(APP_ACTIVITY)
    builder.setMessage(APP_ACTIVITY.getString(R.string.question_delete_reserve))
        .setPositiveButton(APP_ACTIVITY.getString(R.string.yes)) { dialog, _ ->
            EditReserveViewModel(APP_ACTIVITY.application).deleteReserve(reserve)
            dialog.cancel()
            APP_ACTIVITY.supportFragmentManager.popBackStack()
        }
        .setNegativeButton(APP_ACTIVITY.getString(R.string.no)) { dialog, _ ->
            dialog.cancel()
        }
        .show()
}
*/

fun deleteRoomWithDialog(room: RoomData, lo: LifecycleOwner) {
    val viewModel = ViewModelProvider(APP_ACTIVITY).get(RoomDialogViewModel::class.java)
    // если нет бронирований, то не будем ничего спрашивать
    viewModel.getReservesCount(room.id).observe(lo, { count ->
        if (count == 0) {
            viewModel.deleteRoom(room)
            APP_ACTIVITY.supportFragmentManager.popBackStack()
        } else {
            val builder = AlertDialog.Builder(APP_ACTIVITY)
            builder.setMessage(APP_ACTIVITY.getString(R.string.question_delete_room))
                .setPositiveButton(APP_ACTIVITY.getString(R.string.yes)) { dialog, _ ->
                    viewModel.deleteRoom(room)
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


