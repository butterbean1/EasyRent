package ru.butterbean.easyrent.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.butterbean.easyrent.database.TABLE_COSTS_NAME
import ru.butterbean.easyrent.database.TABLE_COST_ITEMS_NAME
import ru.butterbean.easyrent.models.CostData
import ru.butterbean.easyrent.models.CostItemData

@Dao
interface CostItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCostItem(costItem: CostItemData):Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCostItems(costItems: List<CostItemData>)

    @Update
    suspend fun updateCostItem(costItem:CostItemData)

    @Delete()
    suspend fun deleteCostItem(costItem:CostItemData)

    @Query("DELETE FROM $TABLE_COST_ITEMS_NAME")
    suspend fun deleteAllCostItems()

    @Query("SELECT * FROM  $TABLE_COST_ITEMS_NAME ORDER BY name ASC")
    fun getAllCostItems(): LiveData<List<CostItemData>>

    @Query("SELECT * FROM  $TABLE_COST_ITEMS_NAME WHERE id= :id")
    fun getCostItemById(id: Long): CostItemData

}
