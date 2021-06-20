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
interface ReserveArchiveDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addArchiveReserve(reserve:ReserveArchiveData):Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addArchiveReserves(reserves:List<ReserveArchiveData>)

    @Update
    suspend fun updateArchiveReserve(reserve:ReserveArchiveData)

    @Delete
    suspend fun deleteArchiveReserve(reserve:ReserveArchiveData)

    @Delete
    suspend fun deleteArchiveReserves(reserves:List<ReserveArchiveData>)

    @Query("SELECT * FROM $TABLE_ROOMS_NAME WHERE id = :id")
    fun getRoomById(id:Long): LiveData<RoomData>

    @Delete
    suspend fun deleteReserves(reserves : List<ReserveData>)

    @Query("DELETE FROM $TABLE_RESERVES_ARCHIVE_NAME")
    suspend fun deleteAllArchiveReserves()

    @Query("SELECT COUNT(*) FROM $TABLE_RESERVES_ARCHIVE_NAME WHERE roomId = :roomId")
    fun getArchiveReservesCount(roomId:Long): LiveData<Int>

    @Query("SELECT * FROM  $TABLE_RESERVES_ARCHIVE_NAME WHERE roomId= :roomId ORDER BY wasCheckOut,CASE WHEN (wasCheckOut=0) THEN (strftime('%s','now') + strftime('%s',dateCheckIn)) ELSE (strftime('%s','now') - strftime('%s',dateCheckOut)) END")
    fun getReservesByRoomId(roomId: Long): LiveData<List<ReserveArchiveData>>

    @Query("SELECT * FROM  $TABLE_RESERVES_NAME WHERE (date('now','start of day')>date(dateCheckOut,'start of day','+'||:analyseDepth||' days'))&(wasCheckOut=1) ORDER BY dateCheckOut DESC")
    fun getAllCheckOutedReserves(analyseDepth:Int): List<ReserveData>

    @Query("SELECT * FROM $TABLE_ROOMS_NAME ORDER BY name ASC")
    fun readAllRooms(): LiveData<List<RoomData>>


}
