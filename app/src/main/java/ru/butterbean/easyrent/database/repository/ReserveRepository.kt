package ru.butterbean.easyrent.database.repository

import androidx.lifecycle.LiveData
import ru.butterbean.easyrent.database.dao.ReserveDao
import ru.butterbean.easyrent.models.ReserveData
import ru.butterbean.easyrent.models.StatisticQueryResult
import ru.butterbean.easyrent.screens.room.item_models.CommonReserveModel
import ru.butterbean.easyrent.screens.room.item_models.FreeReserveModel
import ru.butterbean.easyrent.screens.room.item_models.SimpleReserveModel
import ru.butterbean.easyrent.utils.*
import java.util.*

class ReserveRepository(private val reserveDao: ReserveDao) {

    suspend fun addReserve(reserve: ReserveData): Long {
        return reserveDao.addReserve(reserve)
    }

    suspend fun deleteReserve(reserve: ReserveData) {
        reserveDao.deleteReserve(reserve)
    }

    suspend fun setAutoCheckInCheckOutForRoom(roomId: Long) {
        val reserves = reserveDao.getActualReservesByRoomId(roomId)
        val updatedReserves = getAutoUpdatedReserves(reserves)
        if (updatedReserves.count() > 0) reserveDao.updateReserves(updatedReserves)
    }

    suspend fun setAutoCheckInCheckOut() {
        val reserves = reserveDao.getAllActualReserves()
        val updatedReserves = getAutoUpdatedReserves(reserves)
        if (updatedReserves.count() > 0) reserveDao.updateReserves(updatedReserves)
    }


    suspend fun updateReserve(reserve: ReserveData) {
        reserveDao.updateReserve(reserve)
    }

    suspend fun updateRoomStatus(roomId: Long) {
        val roomDao = APP_DATABASE.roomDao()
        val room = roomDao.getRoomById(roomId)

        val reservesList = reserveDao.getActualReservesByRoomId(roomId)
        var currentDate = getStartOfDay(Calendar.getInstance())
        var newStatus = STATUS_FREE
        var tmpBusy = false
        for (reserve in reservesList) {
            val dateCheckIn = getCalendarFromString(reserve.dateCheckIn)
            val dateCheckOut = getCalendarFromString(reserve.dateCheckOut)
            if (currentDate.before(getStartOfDay(dateCheckIn))) {
                if (!tmpBusy) {
                    newStatus = "$STATUS_FREE $STATUS_UNTIL ${dateCheckIn.toDateFormat()}"
                    break
                } else {
                    newStatus = "$STATUS_BUSY $STATUS_UNTIL ${currentDate.toDateFormat()}"
                    break
                }
            } else {
                newStatus = "$STATUS_BUSY $STATUS_UNTIL ${dateCheckOut.toDateFormat()}"
                tmpBusy = true
                currentDate = dateCheckOut
            }
        }
        room.status = newStatus
        roomDao.updateRoom(room)
    }

    fun getAllReservesByRoomId(roomId: Long): List<CommonReserveModel> {
        val reservesList = reserveDao.getReservesByRoomId(roomId)
        val resList = mutableListOf<CommonReserveModel>()
        reservesList.forEach { reserve ->
            resList.add(createSimpleModel(reserve))
        }

        return resList
    }

    fun getReservesByRoomId(roomId: Long): List<CommonReserveModel> {
        val reservesList = reserveDao.getReservesByRoomId(roomId)
        // обработаем список резервов и создадим новый список из различных ххххReserveModel (free, simple и т.д.)
        // для вывода в RecyclerView
        var currentDate = Calendar.getInstance()
        currentDate.set(Calendar.MINUTE, 0)
        currentDate.set(Calendar.MILLISECOND, 0)
        var lastFreeShowed = false
        val resList = mutableListOf<CommonReserveModel>()
        reservesList.forEach { reserve ->
            if (reserve.wasCheckOut) {
                if (!lastFreeShowed) {
                    val fm = FreeReserveModel(roomId, currentDate.toDateTimeInDatabaseFormat())
                    resList.add(fm)
                    lastFreeShowed = true
                }
                // дальше для завершенных резервов не нужно заморачиваться - просто выводим simple model
            } else {
                val dateCheckIn = getCalendarFromString(reserve.dateCheckIn)
                val dateCheckOut = getCalendarFromString(reserve.dateCheckOut)
                if (currentDate.before(getStartOfDay(dateCheckIn))) {
                    if (dateCheckIn.get(Calendar.HOUR_OF_DAY) > 12) dateCheckIn.set(
                        Calendar.HOUR_OF_DAY,
                        12
                    )
                    val fm = FreeReserveModel(
                        roomId,
                        currentDate.toDateTimeInDatabaseFormat(),
                        dateCheckIn.toDateTimeInDatabaseFormat()
                    )
                    resList.add(fm)
                }
                if (dateCheckOut.get(Calendar.HOUR_OF_DAY) < 14) dateCheckOut.set(
                    Calendar.HOUR_OF_DAY,
                    14
                )
                currentDate = dateCheckOut
            }
            resList.add(createSimpleModel(reserve))
        }
        if (!lastFreeShowed && reservesList.isNotEmpty()) {
            val fm = FreeReserveModel(roomId, currentDate.toDateTimeInDatabaseFormat())
            resList.add(fm)
        }

        return resList
    }

    private fun createSimpleModel(reserve: ReserveData) =
        SimpleReserveModel(
            reserve.id,
            reserve.roomId,
            reserve.guestName,
            reserve.guestsCount,
            reserve.sum,
            reserve.payment,
            reserve.dateCheckIn,
            reserve.dateCheckOut,
            reserve.wasCheckIn,
            reserve.wasCheckOut,
            reserve.phoneNumber,
            reserve.extFilesCount
        )

    fun getReservesCount(roomId: Long): LiveData<Int> = reserveDao.getReservesCount(roomId)

    fun getReserveById(id: Long): ReserveData = reserveDao.getReserveById(id)

    fun getStatistic(roomId: Long,startDate:String,endDate:String): StatisticQueryResult = reserveDao.getStatistic(roomId,startDate,endDate)


}