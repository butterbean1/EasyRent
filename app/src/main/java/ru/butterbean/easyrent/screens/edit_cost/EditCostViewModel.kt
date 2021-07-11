package ru.butterbean.easyrent.screens.edit_cost

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.butterbean.easyrent.database.repository.CostsRepository
import ru.butterbean.easyrent.models.CostData

class EditCostViewModel(application: Application) : AndroidViewModel(application) {
    private val mRepository = CostsRepository()

    fun addCost(cost: CostData, onSuccess:(Long) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val newId = mRepository.addCost(cost)
            withContext(Dispatchers.Main){onSuccess(newId)}
        }
    }

    fun updateCost(cost: CostData, onSuccess:() -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.updateCost(cost)
            withContext(Dispatchers.Main){onSuccess()}
        }
    }

    fun deleteCost(cost: CostData, onSuccess:() -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.deleteCost(cost)
            withContext(Dispatchers.Main){onSuccess()}
        }
    }

}