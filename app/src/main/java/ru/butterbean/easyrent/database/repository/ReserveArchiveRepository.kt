package ru.butterbean.easyrent.database.repository

import androidx.lifecycle.LiveData
import ru.butterbean.easyrent.database.dao.ReserveArchiveDao
import ru.butterbean.easyrent.models.ReserveArchiveData
import ru.butterbean.easyrent.models.ReserveData
import ru.butterbean.easyrent.models.RoomData
import ru.butterbean.easyrent.utils.getAutoUpdatedReserves
import ru.butterbean.easyrent.utils.reserveCompleted

class ReserveArchiveRepository(private val reserveArchiveDao: ReserveArchiveDao) {

    suspend fun deleteArchiveReserves(reserves: List<ReserveArchiveData>) =
        reserveArchiveDao.deleteArchiveReserves(reserves)

    fun getRoomById(id: Long): LiveData<RoomData> = reserveArchiveDao.getRoomById(id)

    fun readAllRooms(): LiveData<List<RoomData>> = reserveArchiveDao.readAllRooms()

    fun getArchiveReservesByRoomId(roomId: Long): LiveData<List<ReserveArchiveData>> =
        reserveArchiveDao.getReservesByRoomId(roomId)

    suspend fun setAutoCheckInCheckOut() {
        val reserves = reserveArchiveDao.getAllActualReserves()
        val updatedReserves = getAutoUpdatedReserves(reserves)
        if (updatedReserves.count() > 0) reserveArchiveDao.updateReserves(updatedReserves)
    }

    suspend fun replaceReservesToArchive(analyseDepth: Int) {
        val reservesList = reserveArchiveDao.getAllCheckOutedReserves(analyseDepth)
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
        reserveArchiveDao.deleteReserves(reservesToDelete)
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
        reserveArchiveDao.addReserves(newReserves)
    }

}