package ru.butterbean.easyrent.screens.cost_items_list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.butterbean.easyrent.database.repository.CostsRepository
import ru.butterbean.easyrent.models.CostData
import ru.butterbean.easyrent.models.CostItemData

class CostItemsListViewModel(application: Application) : AndroidViewModel(application) {
    private val mRepository = CostsRepository()

    fun getAllCostItems() = mRepository.getAllCostItems()

    fun deleteCostItem(costItem: CostItemData) {
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.deleteCostItem(costItem)
        }
    }
    fun getCostItemById(id:Long,onSuccess: (CostItemData) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val costItem = mRepository.getCostItemById(id)
            withContext(Dispatchers.Main){
                onSuccess(costItem)
            }
        }
    }



}