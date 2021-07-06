package ru.butterbean.easyrent.database.repository

import androidx.lifecycle.LiveData
import ru.butterbean.easyrent.models.ReserveArchiveExtFileData
import ru.butterbean.easyrent.models.ReserveExtFileData
import ru.butterbean.easyrent.utils.APP_DATABASE

class ReserveExtFileRepository {
    private val reserveExtFilesDao = APP_DATABASE.reserveExtFileDao()
    private val reserveArchiveExtFilesDao = APP_DATABASE.reserveArchiveExtFilesDao()

    suspend fun addReserveExtFile(reserveExtFile: ReserveExtFileData): Long {
        return reserveExtFilesDao.addExtFile(reserveExtFile)
    }

     suspend fun deleteReserveExtFile(reserveExtFile: ReserveExtFileData) {
        reserveExtFilesDao.deleteExtFile(reserveExtFile)
    }

    suspend fun addReserveExtFiles(reserveExtFiles: List<ReserveExtFileData>) {
        reserveExtFilesDao.addExtFiles(reserveExtFiles)
    }

    suspend fun deleteReserveExtFiles(reserveExtFiles: List<ReserveExtFileData>) {
        reserveExtFilesDao.deleteExtFiles(reserveExtFiles)
    }

    suspend fun replaceExtFilesToArchive(reserveId: Long, reserveArchiveId: Long) {
        val list = reserveExtFilesDao.getExtFilesByReserveIdNow(reserveId)
        if (list.isNotEmpty()) {
            val archiveFilesList = mutableListOf<ReserveArchiveExtFileData>()
            list.forEach {
                archiveFilesList.add(
                    ReserveArchiveExtFileData(
                        0,
                        reserveArchiveId,
                        it.dirName,
                        it.fileName,
                        it.fileType,
                        it.isImage
                    )
                )
            }
            reserveArchiveExtFilesDao.addExtFiles(archiveFilesList)
            // делаем только добавление файлов в архив, удалятся они сами при удалении записей основной таблицы по foreign key
        }
    }

    suspend fun replaceExtFilesFromArchive(reserveArchiveId: Long, reserveId: Long) {
        val list = reserveArchiveExtFilesDao.getExtFilesByReserveIdNow(reserveId)
        if (list.isNotEmpty()) {
            val filesList = mutableListOf<ReserveExtFileData>()
            list.forEach {
                filesList.add(
                    ReserveExtFileData(
                        0,
                        reserveId,
                        it.dirName,
                        it.fileName,
                        it.fileType,
                        it.isImage
                    )
                )
            }
            reserveExtFilesDao.addExtFiles(filesList)
            // делаем только добавление файлов, удалятся они сами при удалении записей основной таблицы по foreign key
        }
    }

    fun getExtFilesByReserveId(reserveId: Long): LiveData<List<ReserveExtFileData>> =
        reserveExtFilesDao.getExtFilesByReserveId(reserveId)

    fun getExtFilesCount(reserveId: Long): Int = reserveExtFilesDao.getExtFilesCount(reserveId)

    fun getSingleExtFileByReserveId(reserveId: Long): ReserveExtFileData =
        reserveExtFilesDao.getSingleExtFileByReserveId(reserveId)


}