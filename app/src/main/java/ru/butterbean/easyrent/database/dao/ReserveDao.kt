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
    suspend fun addReserves(reserves:List<ReserveData>)

    @Update
    suspend fun updateReserve(reserve:ReserveData)

    @Update
    suspend fun updateReserves(reserve:List<ReserveData>)

    @Delete
    suspend fun deleteReserve(reserve:ReserveData)

    @Delete
    suspend fun deleteReserves(reserves:List<ReserveData>)

    @Query("DELETE FROM $TABLE_RESERVES_NAME")
    suspend fun deleteAllReserves()

    @Query("SELECT * FROM $TABLE_RESERVES_NAME WHERE (date('now','start of day')<=date(dateCheckOut,'start of day')) & (NOT wasCheckOut) ORDER BY dateCheckIn,dateCheckOut ASC")
    fun getAllActualReserves(): List<ReserveData>

    @Query("SELECT * FROM  $TABLE_RESERVES_NAME WHERE (date('now','start of day')>date(dateCheckOut,'start of day','+'||:analyseDepth||' days'))&(wasCheckOut=1) ORDER BY dateCheckOut DESC")
    fun getAllCheckOutedReserves(analyseDepth:Int): List<ReserveData>

    @Query("SELECT * FROM $TABLE_RESERVES_NAME WHERE id = :id")
    fun getReserveById(id:Long): ReserveData

    @Query("SELECT * FROM $TABLE_RESERVES_NAME ORDER BY dateCheckIn ASC")
    fun readAllReserves(): LiveData<List<ReserveData>>

    @Query("SELECT COUNT(*) FROM $TABLE_RESERVES_NAME WHERE roomId = :roomId")
    fun getReservesCount(roomId:Long): LiveData<Int>

    @Query("SELECT * FROM  $TABLE_RESERVES_NAME WHERE roomId= :roomId ORDER BY wasCheckOut,CASE WHEN (wasCheckOut=0) THEN (strftime('%s','now') + strftime('%s',dateCheckIn)) ELSE (strftime('%s','now') - strftime('%s',dateCheckOut)) END")
    fun getReservesByRoomId(roomId: Long): List<ReserveData>

    @Query("SELECT * FROM $TABLE_RESERVES_NAME WHERE (date('now','start of day')<=date(dateCheckOut,'start of day')) & (NOT wasCheckOut) & (roomId= :roomId) ORDER BY dateCheckIn,dateCheckOut ASC")
    fun getActualReservesByRoomId(roomId: Long): List<ReserveData>

}
