package ru.butterbean.easyrent.database.repository

import androidx.lifecycle.LiveData
import ru.butterbean.easyrent.database.dao.ReserveArchiveDao
import ru.butterbean.easyrent.models.ReserveArchiveData
import ru.butterbean.easyrent.models.RoomData

class ReserveArchiveRepository(private val reserveArchiveDao: ReserveArchiveDao) {
    suspend fun addReserve(reserve: ReserveArchiveData): Long {
        return reserveArchiveDao.addArchiveReserve(reserve)
    }

    suspend fun deleteReserve(reserve: ReserveArchiveData) {
        reserveArchiveDao.deleteArchiveReserve(reserve)
    }

    fun getRoomById(id: Long): LiveData<RoomData> = reserveArchiveDao.getRoomById(id)

    fun readAllRooms():LiveData<List<RoomData>> = reserveArchiveDao.readAllRooms()

    fun getArchiveReservesByRoomId(roomId: Long): LiveData<List<ReserveArchiveData>> = reserveArchiveDao.getReservesByRoomId(roomId)

    suspend fun replaceReservesToArchive(analyseDepth:Int){
        val reservesList =  reserveArchiveDao.getAllClosedReserves(analyseDepth)
        reserveArchiveDao.deleteReserves(reservesList)
        val archiveReserves = mutableListOf<ReserveArchiveData>()
        reservesList.forEach{reserve->
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
                reserve.wasCheckOut
            )
            archiveReserves.add(newArchiveReserves)
        }
        reserveArchiveDao.addArchiveReserves(archiveReserves)
    }

}