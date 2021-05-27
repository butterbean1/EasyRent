package ru.butterbean.easyrent

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.butterbean.easyrent.models.RoomData

@Dao
interface RoomDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addRoom(room:RoomData)

    @Update
    suspend fun updateRoom(room:RoomData)

    @Delete
    suspend fun deleteRoom(room:RoomData)

    @Query("DELETE FROM $TABLE_ROOMS_NAME")
    suspend fun deleteAllRooms()

    @Query("SELECT * FROM $TABLE_ROOMS_NAME ORDER BY id ASC")
    fun readAllRooms(): LiveData<List<RoomData>>

}
