package ru.butterbean.easyrent.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.butterbean.easyrent.database.TABLE_RESERVES_ARCHIVE_NAME
import ru.butterbean.easyrent.database.TABLE_RESERVES_NAME
import ru.butterbean.easyrent.database.TABLE_ROOMS_NAME
import ru.butterbean.easyrent.models.ReserveArchiveData
import ru.butterbean.easyrent.models.ReserveData
import ru.butterbean.easyrent.models.RoomData

@Dao
interface ReserveDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addReserve(reserve:ReserveData):Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addReserveArchive(reserve:ReserveArchiveData):Long

    @Update
    suspend fun updateReserve(reserve:ReserveData)

    @Delete
    suspend fun deleteReserve(reserve:ReserveData)

    @Delete
    suspend fun deleteReserveArchive(reserve:ReserveArchiveData)

    @Query("DELETE FROM $TABLE_RESERVES_NAME")
    suspend fun deleteAllReserves()

    @Query("SELECT * FROM $TABLE_ROOMS_NAME WHERE id = :id")
    fun getRoomById(id:Long): LiveData<RoomData>

    @Query("SELECT * FROM $TABLE_RESERVES_NAME ORDER BY dateCheckIn ASC")
    fun readAllReserves(): LiveData<List<ReserveData>>

    @Query("SELECT COUNT(*) FROM $TABLE_RESERVES_NAME WHERE roomId = :roomId")
    fun getReservesCount(roomId:Long): LiveData<Int>

    @Query("SELECT COUNT(*) FROM $TABLE_RESERVES_ARCHIVE_NAME WHERE roomId = :roomId")
    fun getReservesArchiveCount(roomId:Long): Int

    @Query("SELECT * FROM  $TABLE_RESERVES_NAME WHERE roomId= :roomId ORDER BY wasCheckOut,CASE WHEN (wasCheckOut=0) THEN (strftime('%s','now') + strftime('%s',dateCheckIn)) ELSE (strftime('%s','now') - strftime('%s',dateCheckOut)) END")
    fun getReservesByRoomId(roomId: Long): List<ReserveData>

    @Query("SELECT * FROM $TABLE_RESERVES_NAME WHERE (date('now','start of day')<=date(dateCheckOut,'start of day')) & (NOT wasCheckOut) & (roomId= :roomId) ORDER BY dateCheckIn,dateCheckOut ASC")
    fun getActualReservesByRoomId(roomId: Long): List<ReserveData>

}
