package ru.butterbean.easyrent.screens.edit_cost_item

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.butterbean.easyrent.database.repository.CostsRepository
import ru.butterbean.easyrent.models.CostData
import ru.butterbean.easyrent.models.CostItemData

class EditCostItemViewModel(application: Application) : AndroidViewModel(application) {
    private val mRepository = CostsRepository()

    fun addCostItem(costItem: CostItemData, onSuccess:(Long) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val newId = mRepository.addCostItem(costItem)
            withContext(Dispatchers.Main){onSuccess(newId)}
        }
    }

    fun updateCostItem(costItem: CostItemData, onSuccess:() -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.updateCostItem(costItem)
            withContext(Dispatchers.Main){onSuccess()}
        }
    }

    fun deleteCostItem(costItem: CostItemData, onSuccess:() -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.deleteCostItem(costItem)
            withContext(Dispatchers.Main){onSuccess()}
        }
    }

    fun getCostItemById(costItemId: Long, onSuccess:(CostItemData) -> Unit) {
        mRepository.getCostById(costItemId)
    }

}