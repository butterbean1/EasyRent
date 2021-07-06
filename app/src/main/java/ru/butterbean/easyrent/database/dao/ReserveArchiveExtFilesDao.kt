package ru.butterbean.easyrent.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.butterbean.easyrent.database.TABLE_RESERVES_ARCHIVE_EXT_FILES_NAME
import ru.butterbean.easyrent.models.ReserveArchiveExtFileData

@Dao
interface ReserveArchiveExtFilesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addExtFile(extFile:ReserveArchiveExtFileData):Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addExtFiles(extFiles:List<ReserveArchiveExtFileData>)

    @Update
    suspend fun updateExtFile(extFile:ReserveArchiveExtFileData)

    @Update
    suspend fun updateExtFiles(extFile:List<ReserveArchiveExtFileData>)

    @Delete
    suspend fun deleteExtFile(extFile:ReserveArchiveExtFileData)

    @Delete
    suspend fun deleteExtFiles(extFiles:List<ReserveArchiveExtFileData>)

    @Query("SELECT COUNT(*) FROM $TABLE_RESERVES_ARCHIVE_EXT_FILES_NAME WHERE reserveId = :reserveId")
    fun getExtFilesCount(reserveId:Long): Int

    @Query("SELECT * FROM  $TABLE_RESERVES_ARCHIVE_EXT_FILES_NAME WHERE reserveId= :reserveId ORDER BY dirName")
    fun getExtFilesByReserveId(reserveId: Long): LiveData<List<ReserveArchiveExtFileData>>

    @Query("SELECT * FROM  $TABLE_RESERVES_ARCHIVE_EXT_FILES_NAME WHERE reserveId= :reserveId ORDER BY dirName")
    fun getExtFilesByReserveIdNow(reserveId: Long): List<ReserveArchiveExtFileData>

}
