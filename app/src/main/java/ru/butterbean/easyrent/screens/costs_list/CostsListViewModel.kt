package ru.butterbean.easyrent.screens.costs_list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.butterbean.easyrent.database.repository.CostsRepository
import ru.butterbean.easyrent.database.repository.ReserveExtFileRepository
import ru.butterbean.easyrent.database.repository.ReserveRepository
import ru.butterbean.easyrent.models.CostData
import ru.butterbean.easyrent.models.ReserveData
import ru.butterbean.easyrent.models.ReserveExtFileData
import ru.butterbean.easyrent.utils.APP_DATABASE

class CostsListViewModel(application: Application) : AndroidViewModel(application) {
    private val mRepository = CostsRepository()

    fun getCostsByRoomId(roomId: Long) = mRepository.getCostsByRoomId(roomId)

    fun deleteCost(cost: CostData) {
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.deleteCost(cost)

        }
    }
}