package ru.butterbean.easyrent.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.butterbean.easyrent.database.TABLE_RESERVES_EXT_FILES_NAME
import ru.butterbean.easyrent.models.ReserveExtFileData

@Dao
interface ReserveExtFilesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addExtFile(extFile:ReserveExtFileData):Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addExtFiles(extFiles:List<ReserveExtFileData>)

    @Update
    suspend fun updateExtFile(extFile:ReserveExtFileData)

    @Update
    suspend fun updateExtFiles(extFile:List<ReserveExtFileData>)

    @Delete
    suspend fun deleteExtFile(extFile:ReserveExtFileData)

    @Delete
    suspend fun deleteExtFiles(extFiles:List<ReserveExtFileData>)

    @Query("SELECT COUNT(*) FROM $TABLE_RESERVES_EXT_FILES_NAME WHERE reserveId = :reserveId")
    fun getExtFilesCount(reserveId:Long): Int

    @Query("SELECT * FROM  $TABLE_RESERVES_EXT_FILES_NAME WHERE reserveId= :reserveId ORDER BY dirName")
    fun getExtFilesByReserveId(reserveId: Long): LiveData<List<ReserveExtFileData>>

}
