package ru.butterbean.easyrent.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.butterbean.easyrent.database.TABLE_RESERVES_NAME
import ru.butterbean.easyrent.database.TABLE_ROOMS_NAME
import ru.butterbean.easyrent.models.RoomData

@Dao
interface RoomDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addRoom(room:RoomData) :Long

    @Update
    suspend fun updateRoom(room:RoomData)

    @Delete
    suspend fun deleteRoom(room:RoomData)

    @Query("DELETE FROM $TABLE_ROOMS_NAME")
    suspend fun deleteAllRooms()

    @Query("SELECT * FROM $TABLE_ROOMS_NAME ORDER BY name ASC")
    fun readAllRooms(): LiveData<List<RoomData>>

    @Query("SELECT COUNT(*) FROM $TABLE_RESERVES_NAME WHERE roomId = :roomId")
    fun getReservesCount(roomId:Long): LiveData<Int>

    @Query("SELECT * FROM $TABLE_ROOMS_NAME WHERE id = :id")
    fun getByIdNow(id:Long): RoomData

    @Query("SELECT Status FROM $TABLE_ROOMS_NAME WHERE id = :id")
    fun getStatus(id: Long): LiveData<String>


}
