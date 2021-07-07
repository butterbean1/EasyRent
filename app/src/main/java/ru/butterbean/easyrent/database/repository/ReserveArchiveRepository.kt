package ru.butterbean.easyrent.database.repository

import androidx.lifecycle.LiveData
import ru.butterbean.easyrent.models.ReserveArchiveData
import ru.butterbean.easyrent.models.ReserveData
import ru.butterbean.easyrent.utils.APP_DATABASE
import ru.butterbean.easyrent.utils.reserveCompleted

class ReserveArchiveRepository{
    private val reserveArchiveDao = APP_DATABASE.reserveArchiveDao()
    private val reserveDao = APP_DATABASE.reserveDao()

    suspend fun addReserveArchive(reserve: ReserveArchiveData): Long {
        return reserveArchiveDao.addArchiveReserve(reserve)
    }

    suspend fun deleteArchiveReserves(reserves: List<ReserveArchiveData>) =
        reserveArchiveDao.deleteArchiveReserves(reserves)

    suspend fun deleteArchiveReserve(reserve: ReserveArchiveData) =
        reserveArchiveDao.deleteArchiveReserve(reserve)

    fun getArchiveReservesByRoomId(roomId: Long): LiveData<List<ReserveArchiveData>> =
        reserveArchiveDao.getArchiveReservesByRoomId(roomId)

    suspend fun replaceReservesToArchive(analyseDepth: Int) {
        val reservesList = reserveDao.getAllCheckOutedReserves(analyseDepth)
        val reservesToDelete = mutableListOf<ReserveData>()
        val archiveReserves = mutableListOf<ReserveArchiveData>()
        reservesList.forEach { reserve ->
            if (reserveCompleted(reserve.wasCheckOut, reserve.sum, reserve.payment)) {
                val newArchiveReserves = getReserveArchiveDataFromReserve(reserve)
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
            val newReserve = getReserveDataFromArchive(reserve)
            newReserves.add(newReserve)
        }
        reserveArchiveDao.deleteArchiveReserves(reservesList)
        reserveDao.addReserves(newReserves)
    }

    suspend fun replaceReserveToArchive(reserve: ReserveData):Long {
        val newArchiveReserve = getReserveArchiveDataFromReserve(reserve)
        return reserveArchiveDao.addArchiveReserve(newArchiveReserve)

    }

    suspend fun replaceReserveFromArchive(reserve: ReserveArchiveData):Long {
        val newReserve = getReserveDataFromArchive(reserve)
        val newId = reserveDao.addReserve(newReserve)
        return newId
    }

    private fun getReserveArchiveDataFromReserve(reserve: ReserveData) =
        ReserveArchiveData(
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
            reserve.phoneNumber,
            reserve.extFilesCount
        )

    private fun getReserveDataFromArchive(reserve: ReserveArchiveData) =
        ReserveData(
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
            reserve.phoneNumber,
            reserve.extFilesCount
        )

    fun getReservesArchiveCount(roomId: Long): Long = reserveArchiveDao.getArchiveReservesCount(roomId)


}