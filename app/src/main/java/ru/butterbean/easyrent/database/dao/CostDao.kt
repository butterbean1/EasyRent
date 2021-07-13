package ru.butterbean.easyrent.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.butterbean.easyrent.database.TABLE_COSTS_NAME
import ru.butterbean.easyrent.database.TABLE_COST_ITEMS_NAME
import ru.butterbean.easyrent.models.CostData
import ru.butterbean.easyrent.screens.costs_list.CostsListItem

@Dao
interface CostDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCost(cost: CostData): Long

    @Update
    suspend fun updateCost(cost: CostData)

    @Delete
    suspend fun deleteCost(cost: CostData)

    @Query("DELETE FROM $TABLE_COSTS_NAME")
    suspend fun deleteAllCosts()

    @Query("SELECT costs.id id,costs.sum sum,costs.date date,items.name itemName FROM  $TABLE_COSTS_NAME costs LEFT OUTER JOIN $TABLE_COST_ITEMS_NAME items ON costs.costItemId=items.id  WHERE roomId= :roomId ORDER BY date DESC")
    fun getCostsByRoomId(roomId: Long): LiveData<List<CostsListItem>>

    @Query("SELECT * FROM  $TABLE_COSTS_NAME WHERE id= :id")
    fun getCostById(id: Long): CostData

}
