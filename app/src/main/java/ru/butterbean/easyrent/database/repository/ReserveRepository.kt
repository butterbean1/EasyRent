package ru.butterbean.easyrent.database.repository

import androidx.lifecycle.LiveData
import ru.butterbean.easyrent.database.STATUS_BUSY
import ru.butterbean.easyrent.database.STATUS_FREE
import ru.butterbean.easyrent.database.dao.ReserveDao
import ru.butterbean.easyrent.database.models.ReserveData
import ru.butterbean.easyrent.utils.APP_DATABASE
import ru.butterbean.easyrent.utils.getCalendarFromString
import ru.butterbean.easyrent.utils.getStartOfDay
import ru.butterbean.easyrent.utils.toDateFormat
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
                    newStatus = "$STATUS_FREE до ${dateCheckIn.toDateFormat()}"
                    break
                }else{
                    newStatus = "$STATUS_BUSY до ${currentDate.toDateFormat()}"
                    break
                }
            }else {
                newStatus = "$STATUS_BUSY до ${dateCheckOut.toDateFormat()}"
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