package ru.butterbean.easyrent.database.repository

import androidx.lifecycle.LiveData
import ru.butterbean.easyrent.database.dao.ReserveArchiveExtFilesDao
import ru.butterbean.easyrent.models.ReserveArchiveExtFileData

class ReserveArchiveExtFileRepository(private val reserveArchiveExtFilesDao: ReserveArchiveExtFilesDao) {
    suspend fun addReserveExtFile(reserveExtFile: ReserveArchiveExtFileData): Long {
        return reserveArchiveExtFilesDao.addExtFile(reserveExtFile)
    }

    suspend fun updateReserveExtFile(reserveExtFile: ReserveArchiveExtFileData) {
        return reserveArchiveExtFilesDao.updateExtFile(reserveExtFile)
    }

    suspend fun deleteReserveExtFile(reserveExtFile: ReserveArchiveExtFileData) {
        return reserveArchiveExtFilesDao.deleteExtFile(reserveExtFile)
    }

    suspend fun addReserveExtFiles(reserveExtFiles: List<ReserveArchiveExtFileData>) {
        return reserveArchiveExtFilesDao.addExtFiles(reserveExtFiles)
    }

    suspend fun updateReserveExtFiles(reserveExtFiles: List<ReserveArchiveExtFileData>) {
        return reserveArchiveExtFilesDao.updateExtFiles(reserveExtFiles)
    }

    suspend fun deleteReserveExtFiles(reserveExtFiles: List<ReserveArchiveExtFileData>) {
        return reserveArchiveExtFilesDao.deleteExtFiles(reserveExtFiles)
    }

    fun getExtFilesByReserveId(reserveId: Long): LiveData<List<ReserveArchiveExtFileData>> = reserveArchiveExtFilesDao.getExtFilesByReserveId(reserveId)

}