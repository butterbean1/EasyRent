package ru.butterbean.easyrent.database.repository

import androidx.lifecycle.LiveData
import ru.butterbean.easyrent.database.dao.ReserveDao
import ru.butterbean.easyrent.database.models.ReserveData
import ru.butterbean.easyrent.utils.*
import java.util.*

class ReserveRepository(private val reserveDao: ReserveDao) {

    val readAllReserves: LiveData<List<ReserveData>> = reserveDao.readAllReserves()

    suspend fun addReserve(reserve: ReserveData): Long {
        return reserveDao.addReserve(reserve)
    }

    suspend fun deleteReserve(reserve: ReserveData) {
        reserveDao.deleteReserve(reserve)
    }

    suspend fun updateReserve(reserve: ReserveData) {
        reserveDao.updateReserve(reserve)
    }

    suspend fun updateRoomStatus(roomId: Long) {
        val roomDao = APP_DATABASE.roomDao()
        val room = roomDao.getByIdNow(roomId)

        val reservesList = reserveDao.getActualReservesByRoomId(roomId)
        var currentDate = getStartOfDay(Calendar.getInstance())
        var newStatus = STATUS_FREE
        var tmpBusy = false
        for (reserve in reservesList){
            val dateCheckIn = getCalendarFromString(reserve.dateCheckIn)
            val dateCheckOut = getCalendarFromString(reserve.dateCheckOut)
            if (currentDate.before(getStartOfDay(dateCheckIn))) {
                if (!tmpBusy) {
                    newStatus = "$STATUS_FREE $STATUS_UNTIL ${dateCheckIn.toDateFormat()}"
                    break
                }else{
                    newStatus = "$STATUS_BUSY $STATUS_UNTIL ${currentDate.toDateFormat()}"
                    break
                }
            }else {
                newStatus = "$STATUS_BUSY $STATUS_UNTIL ${dateCheckOut.toDateFormat()}"
                tmpBusy = true
                currentDate = dateCheckOut
            }
        }
        room.status = newStatus
        roomDao.updateRoom(room)
    }

    suspend fun deleteAllReserves() {
        reserveDao.deleteAllReserves()
    }

    fun getReservesByRoomId(roomId: Long): LiveData<List<ReserveData>> =
        reserveDao.getReservesByRoomId(roomId)


}