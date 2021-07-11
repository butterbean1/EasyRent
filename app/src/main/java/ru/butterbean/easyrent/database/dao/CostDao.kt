package ru.butterbean.easyrent.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.butterbean.easyrent.database.TABLE_COSTS_NAME
import ru.butterbean.easyrent.models.CostData

@Dao
interface CostDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCost(guest: CostData):Long

    @Update
    suspend fun updateCost(guest:CostData)

    @Delete
    suspend fun deleteCost(guest:CostData)

    @Query("DELETE FROM $TABLE_COSTS_NAME")
    suspend fun deleteAllCosts()

    @Query("SELECT * FROM  $TABLE_COSTS_NAME WHERE roomId= :roomId ORDER BY date DESC")
    fun getCostsByRoomId(roomId: Long): LiveData<List<CostData>>


}
