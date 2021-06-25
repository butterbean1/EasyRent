package ru.butterbean.easyrent.database.repository

import androidx.lifecycle.LiveData
import ru.butterbean.easyrent.database.dao.ReserveArchiveDao
import ru.butterbean.easyrent.database.dao.ReserveDao
import ru.butterbean.easyrent.database.dao.RoomDao
import ru.butterbean.easyrent.models.ReserveArchiveData
import ru.butterbean.easyrent.models.ReserveData
import ru.butterbean.easyrent.models.RoomData
import ru.butterbean.easyrent.utils.reserveCompleted

class ReserveArchiveRepository(
    private val reserveArchiveDao: ReserveArchiveDao,
    private val roomDao: RoomDao,
    private val reserveDao: ReserveDao
) {

    suspend fun deleteArchiveReserves(reserves: List<ReserveArchiveData>) =
        reserveArchiveDao.deleteArchiveReserves(reserves)

    fun readAllRooms(): LiveData<List<RoomData>> = roomDao.readAllRooms()

    fun getArchiveReservesByRoomId(roomId: Long): LiveData<List<ReserveArchiveData>> =
        reserveArchiveDao.getReservesByRoomId(roomId)

    suspend fun replaceReservesToArchive(analyseDepth: Int) {
        val reservesList = reserveDao.getAllCheckOutedReserves(analyseDepth)
        val reservesToDelete = mutableListOf<ReserveData>()
        val archiveReserves = mutableListOf<ReserveArchiveData>()
        reservesList.forEach { reserve ->
            if (reserveCompleted(reserve.wasCheckOut, reserve.sum, reserve.payment)) {
                val newArchiveReserves = ReserveArchiveData(
                    0,
                    reserve.roomId,
                    reserve.guestName,
                    reserve.guestsCount,
                    reserve.sum,
                    reserve.payment,
                    reserve.dateCheckIn,
                    reserve.dateCheckOut,
                    reserve.wasCheckIn,
                    reserve.wasCheckOut,
                    reserve.phoneNumber
                )
                archiveReserves.add(newArchiveReserves)
                reservesToDelete.add(reserve)
            }
        }
        reserveDao.deleteReserves(reservesToDelete)
        reserveArchiveDao.addArchiveReserves(archiveReserves)
    }

    suspend fun replaceReservesFromArchive(reservesList: List<ReserveArchiveData>) {
        val newReserves = mutableListOf<ReserveData>()
        reservesList.forEach { reserve ->
            val newReserve = ReserveData(
                0,
                reserve.roomId,
                reserve.guestName,
                reserve.guestsCount,
                reserve.sum,
                reserve.payment,
                reserve.dateCheckIn,
                reserve.dateCheckOut,
                reserve.wasCheckIn,
                reserve.wasCheckOut,
                reserve.phoneNumber
            )
            newReserves.add(newReserve)
        }
        reserveArchiveDao.deleteArchiveReserves(reservesList)
        reserveDao.addReserves(newReserves)
    }

    suspend fun replaceReserveToArchive(reserve: ReserveData) {
        val newArchiveReserve = ReserveArchiveData(
            0,
            reserve.roomId,
            reserve.guestName,
            reserve.guestsCount,
            reserve.sum,
            reserve.payment,
            reserve.dateCheckIn,
            reserve.dateCheckOut,
            reserve.wasCheckIn,
            reserve.wasCheckOut,
            reserve.phoneNumber
        )
        reserveDao.deleteReserve(reserve)
        reserveArchiveDao.addArchiveReserve(newArchiveReserve)
    }

    suspend fun replaceReserveFromArchive(reserve: ReserveArchiveData):Long {
        val newReserve = ReserveData(
            0,
            reserve.roomId,
            reserve.guestName,
            reserve.guestsCount,
            reserve.sum,
            reserve.payment,
            reserve.dateCheckIn,
            reserve.dateCheckOut,
            reserve.wasCheckIn,
            reserve.wasCheckOut,
            reserve.phoneNumber
        )
        reserveArchiveDao.deleteArchiveReserve(reserve)
        return reserveDao.addReserve(newReserve)
    }


}