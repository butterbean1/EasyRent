package ru.butterbean.easyrent.utils

import android.app.AlertDialog
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import ru.butterbean.easyrent.R
import ru.butterbean.easyrent.models.ReserveData
import ru.butterbean.easyrent.models.RoomData
import ru.butterbean.easyrent.screens.RoomDialogViewModel
import java.util.*

fun getEmptyRoom(): RoomData {
    return RoomData(0,"","", STATUS_FREE)
}

fun getEmptyReserve(roomId: Long): ReserveData {
    return ReserveData(0, roomId)
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

fun deleteRoomWithDialog(room: RoomData, lo: LifecycleOwner, onSuccess: (Boolean) -> Unit) {
    val viewModel = ViewModelProvider(APP_ACTIVITY).get(RoomDialogViewModel::class.java)
    // если нет бронирований, то не будем ничего спрашивать
    viewModel.getReservesCount(room.id).observe(lo, { count ->
        if (count == 0) {
            viewModel.deleteRoom(room){onSuccess(true)}

        } else {
            val builder = AlertDialog.Builder(APP_ACTIVITY)
            builder.setMessage(APP_ACTIVITY.getString(R.string.question_delete_room))
                .setPositiveButton(APP_ACTIVITY.getString(R.string.yes)) { dialog, _ ->
                    dialog.cancel()
                    viewModel.deleteRoom(room){onSuccess(true)}
                }
                .setNegativeButton(APP_ACTIVITY.getString(R.string.no)) { dialog, _ ->
                    dialog.cancel()
                    onSuccess(false)
                }
                .show()
        }
    })
}

fun getAutoUpdatedReserves(reserves: List<ReserveData>): List<ReserveData> {
    val updatedReserves = mutableListOf<ReserveData>()
    reserves.forEach { reserve ->
        var wasChanges = false
        var newWasCheckIn = reserve.wasCheckIn
        var newWasCheckOut = reserve.wasCheckOut
        val currentCal = Calendar.getInstance()
        if (!reserve.wasCheckIn && currentCal.after(getCalendarFromString(reserve.dateCheckIn))) {
            wasChanges = true
            newWasCheckIn = true
        }
        if (!reserve.wasCheckOut && currentCal.after(getCalendarFromString(reserve.dateCheckOut))) {
            wasChanges = true
            newWasCheckOut = true
        }
        if (wasChanges) {
            updatedReserves.add(
                ReserveData(
                    reserve.id,
                    reserve.roomId,
                    reserve.guestName,
                    reserve.guestsCount,
                    reserve.sum,
                    reserve.payment,
                    reserve.dateCheckIn,
                    reserve.dateCheckOut,
                    newWasCheckIn,
                    newWasCheckOut

                )
            )
        }
    }
    return updatedReserves
}
