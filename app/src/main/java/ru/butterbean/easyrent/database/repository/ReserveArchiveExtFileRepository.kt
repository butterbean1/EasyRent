package ru.butterbean.easyrent.database.repository

import androidx.lifecycle.LiveData
import ru.butterbean.easyrent.database.dao.ReserveArchiveExtFilesDao
import ru.butterbean.easyrent.models.ReserveArchiveExtFileData

class ReserveArchiveExtFileRepository(private val reserveArchiveExtFilesDao: ReserveArchiveExtFilesDao) {
    suspend fun deleteReserveExtFile(reserveExtFile: ReserveArchiveExtFileData) {
        return reserveArchiveExtFilesDao.deleteExtFile(reserveExtFile)
    }

    suspend fun addReserveExtFiles(reserveExtFiles: List<ReserveArchiveExtFileData>) {
        return reserveArchiveExtFilesDao.addExtFiles(reserveExtFiles)
    }

    suspend fun deleteReserveExtFiles(reserveExtFiles: List<ReserveArchiveExtFileData>) {
        return reserveArchiveExtFilesDao.deleteExtFiles(reserveExtFiles)
    }

    fun getExtFileDirsByReserveId(reserveId: Long): List<String> =
        reserveArchiveExtFilesDao.getExtFileDirsByReserveId(reserveId)

    fun getExtFilesByReserveId(reserveId: Long): LiveData<List<ReserveArchiveExtFileData>> = reserveArchiveExtFilesDao.getExtFilesByReserveId(reserveId)

    fun getSingleExtFileByReserveId(reserveId: Long): ReserveArchiveExtFileData =
        reserveArchiveExtFilesDao.getSingleExtFileByReserveId(reserveId)

    fun getExtFilesCount(reserveId: Long): Int = reserveArchiveExtFilesDao.getExtFilesCount(reserveId)

}