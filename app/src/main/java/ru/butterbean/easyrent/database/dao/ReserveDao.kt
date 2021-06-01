package ru.butterbean.easyrent.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.butterbean.easyrent.database.TABLE_RESERVES_NAME
import ru.butterbean.easyrent.models.ReserveData

@Dao
interface ReserveDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addReserve(reserve:ReserveData)

    @Update
    suspend fun updateReserve(reserve:ReserveData)

    @Delete
    suspend fun deleteReserve(reserve:ReserveData)

    @Query("DELETE FROM $TABLE_RESERVES_NAME")
    suspend fun deleteAllReserves()

    @Query("SELECT * FROM $TABLE_RESERVES_NAME ORDER BY id ASC")
    fun readAllReserves(): LiveData<List<ReserveData>>

}
