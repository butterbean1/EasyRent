package ru.butterbean.easyrent.database.repository

import androidx.lifecycle.LiveData
import ru.butterbean.easyrent.database.dao.ReserveExtFilesDao
import ru.butterbean.easyrent.models.ReserveExtFileData

class ReserveExtFileRepository(private val reserveExtFilesDao: ReserveExtFilesDao) {
    suspend fun addReserveExtFile(reserveExtFile: ReserveExtFileData): Long {
        return reserveExtFilesDao.addExtFile(reserveExtFile)
    }

    suspend fun updateReserveExtFile(reserveExtFile: ReserveExtFileData) {
        return reserveExtFilesDao.updateExtFile(reserveExtFile)
    }

    suspend fun deleteReserveExtFile(reserveExtFile: ReserveExtFileData) {
        return reserveExtFilesDao.deleteExtFile(reserveExtFile)
    }

    suspend fun addReserveExtFiles(reserveExtFiles: List<ReserveExtFileData>) {
        return reserveExtFilesDao.addExtFiles(reserveExtFiles)
    }

    suspend fun updateReserveExtFiles(reserveExtFiles: List<ReserveExtFileData>) {
        return reserveExtFilesDao.updateExtFiles(reserveExtFiles)
    }

    suspend fun deleteReserveExtFiles(reserveExtFiles: List<ReserveExtFileData>) {
        return reserveExtFilesDao.deleteExtFiles(reserveExtFiles)
    }

    fun getExtFilesByReserveId(reserveId: Long): LiveData<List<ReserveExtFileData>> = reserveExtFilesDao.getExtFilesByReserveId(reserveId)

}