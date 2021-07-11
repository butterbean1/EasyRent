package ru.butterbean.easyrent.database.repository

import androidx.lifecycle.LiveData
import ru.butterbean.easyrent.models.CostData
import ru.butterbean.easyrent.models.CostItemData
import ru.butterbean.easyrent.screens.costs_list.CostsListItem
import ru.butterbean.easyrent.utils.APP_DATABASE

class CostsRepository {
    private val costDao = APP_DATABASE.costDao()
    private val costItemDao = APP_DATABASE.costItemDao()

    suspend fun addCost(cost: CostData): Long {
        return costDao.addCost(cost)
    }

    suspend fun updateCost(cost: CostData) {
        costDao.updateCost(cost)
    }

    suspend fun deleteCost(cost: CostData) {
        costDao.deleteCost(cost)
    }

    fun getCostsByRoomId(roomId: Long): LiveData<List<CostsListItem>> =
        costDao.getCostsByRoomId(roomId)

    fun getCostById(id: Long): CostData = costDao.getCostById(id)


    suspend fun addCostItem(costItem: CostItemData): Long {
        return costItemDao.addCostItem(costItem)
    }

    suspend fun updateCostItem(costItem: CostItemData) {
        costItemDao.updateCostItem(costItem)
    }

    suspend fun deleteCostItem(costItem: CostItemData) {
        costItemDao.deleteCostItem(costItem)
    }

    fun getAllCostItems(): LiveData<List<CostItemData>> =
        costItemDao.getAllCostItems()
}