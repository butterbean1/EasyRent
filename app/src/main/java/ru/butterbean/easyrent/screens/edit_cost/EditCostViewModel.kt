package ru.butterbean.easyrent.screens.edit_cost

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.butterbean.easyrent.database.repository.CostsRepository
import ru.butterbean.easyrent.database.repository.RoomRepository
import ru.butterbean.easyrent.models.CostData
import ru.butterbean.easyrent.models.CostItemData
import ru.butterbean.easyrent.models.RoomData
import ru.butterbean.easyrent.utils.APP_DATABASE

class EditCostViewModel(application: Application) : AndroidViewModel(application) {
    private val mRepository = CostsRepository()
    private val mRoomRepository = RoomRepository(APP_DATABASE.roomDao())

    fun addCost(cost: CostData, onSuccess: (Long) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val newId = mRepository.addCost(cost)
            withContext(Dispatchers.Main) { onSuccess(newId) }
        }
    }

    fun updateCost(cost: CostData, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.updateCost(cost)
            withContext(Dispatchers.Main) { onSuccess() }
        }
    }

    fun deleteCost(cost: CostData, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.deleteCost(cost)
            withContext(Dispatchers.Main) { onSuccess() }
        }
    }

    fun getCostItemById(costItemId: Long, onSuccess: (CostItemData) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val costItem = mRepository.getCostItemById(costItemId)
            withContext(Dispatchers.Main) { onSuccess(costItem) }
        }
    }

    fun getRoomById(id: Long, onSuccess: (RoomData) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val room = mRoomRepository.getRoomById(id)
            withContext(Dispatchers.Main) {
                onSuccess(room)
            }
        }
    }



}