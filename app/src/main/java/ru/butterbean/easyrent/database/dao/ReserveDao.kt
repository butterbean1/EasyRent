package ru.butterbean.easyrent.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.butterbean.easyrent.database.TABLE_RESERVES_NAME
import ru.butterbean.easyrent.database.models.ReserveData

@Dao
interface ReserveDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addReserve(reserve:ReserveData):Long

    @Update
    suspend fun updateReserve(reserve:ReserveData)

    @Delete
    suspend fun deleteReserve(reserve:ReserveData)

    @Query("DELETE FROM $TABLE_RESERVES_NAME")
    suspend fun deleteAllReserves()

    @Query("SELECT * FROM $TABLE_RESERVES_NAME ORDER BY id ASC")
    fun readAllReserves(): LiveData<List<ReserveData>>

    @Query("SELECT * FROM $TABLE_RESERVES_NAME WHERE roomId= :roomId ORDER BY id ASC")
    fun getReservesByRoomId(roomId: Int): LiveData<List<ReserveData>>

    @Query("SELECT * FROM $TABLE_RESERVES_NAME WHERE date('now','start of day')==date(dateCheckOut,'start of day') & roomId= :roomId ORDER BY dateCheckIn ASC")
    fun getActualReservesByRoomId(roomId: Int): LiveData<List<ReserveData>>

    @Query("SELECT * FROM $TABLE_RESERVES_NAME WHERE date(roomId,'start of day')==date(dateCheckOut,'start of day') & roomId= :roomId ORDER BY dateCheckIn ASC")
    fun getEqualseservesByRoomId(roomId: Int): LiveData<List<ReserveData>>

}
