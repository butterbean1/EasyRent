package ru.butterbean.easyrent.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.butterbean.easyrent.TABLE_GUESTS_NAME
import ru.butterbean.easyrent.models.GuestData

@Dao
interface GuestDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addGuest(guest:GuestData)

    @Update
    suspend fun updateGuest(guest:GuestData)

    @Delete
    suspend fun deleteGuest(guest:GuestData)

    @Query("DELETE FROM $TABLE_GUESTS_NAME")
    suspend fun deleteAllGuests()

    @Query("SELECT * $TABLE_GUESTS_NAME ORDER BY id ASC")
    fun readAllGuests(): LiveData<List<GuestData>>

}
