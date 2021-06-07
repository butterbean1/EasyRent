package ru.butterbean.easyrent.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.butterbean.easyrent.database.TABLE_GUESTS_NAME
import ru.butterbean.easyrent.models.GuestData

@Dao
interface GuestDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addGuest(guest:GuestData):Long

    @Update
    suspend fun updateGuest(guest:GuestData)

    @Delete
    suspend fun deleteGuest(guest:GuestData)

    @Query("DELETE FROM $TABLE_GUESTS_NAME")
    suspend fun deleteAllGuests()

    @Query("SELECT * FROM $TABLE_GUESTS_NAME ORDER BY id ASC")
    fun readAllGuests(): LiveData<List<GuestData>>

    @Query("SELECT * FROM $TABLE_GUESTS_NAME WHERE id = :id")
    fun getById(id:Long): GuestData

}
