package ru.butterbean.easyrent.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.butterbean.easyrent.database.TABLE_COST_ITEMS_NAME
import ru.butterbean.easyrent.models.CostItemData

@Dao
interface CostItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCostItem(guest: CostItemData):Long

    @Update
    suspend fun updateCostItem(guest:CostItemData)

    @Delete
    suspend fun deleteCostItem(guest:CostItemData)

    @Query("DELETE FROM $TABLE_COST_ITEMS_NAME")
    suspend fun deleteAllCostItems()

    @Query("SELECT * FROM  $TABLE_COST_ITEMS_NAME ORDER BY name ASC")
    fun getAllCostItems(): LiveData<List<CostItemData>>

}
