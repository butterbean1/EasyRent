package ru.butterbean.easyrent.screens.costs_list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.butterbean.easyrent.database.repository.CostsRepository
import ru.butterbean.easyrent.models.CostData

class CostsListViewModel(application: Application) : AndroidViewModel(application) {
    private val mRepository = CostsRepository()

    fun getCostsByRoomId(roomId: Long) = mRepository.getCostsByRoomId(roomId)

    fun deleteCost(cost: CostData) {
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.deleteCost(cost)
        }
    }
    fun getCostById(id:Long,onSuccess: (CostData) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val cost = mRepository.getCostById(id)
            withContext(Dispatchers.Main){
                onSuccess(cost)
            }
        }
    }



}