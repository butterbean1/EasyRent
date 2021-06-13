package ru.butterbean.easyrent.database.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.butterbean.easyrent.database.repository.ReserveRepository
import ru.butterbean.easyrent.models.ReserveData
import ru.butterbean.easyrent.models.RoomData
import ru.butterbean.easyrent.screens.reserves.ReserveType
import ru.butterbean.easyrent.utils.APP_DATABASE

class RoomViewModel(application: Application) : AndroidViewModel(application) {
    private val mRepository: ReserveRepository = ReserveRepository(APP_DATABASE.reserveDao())

    fun deleteReserve(reserve: ReserveData, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.deleteReserve(reserve)
            mRepository.updateRoomStatus(reserve.roomId)
            withContext(Dispatchers.Main) {
                onSuccess()
            }
        }
    }

    fun getReservesCount(roomId: Long): LiveData<Int> = mRepository.getReservesCount(roomId)

    fun getReservesByRoomId(
        roomId: Long,
        dontShowFreeReserves: Boolean,
        onSuccess: (List<ReserveType>) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val list: List<ReserveType> = if (dontShowFreeReserves) mRepository.getAllReservesByRoomId(roomId)
                   else mRepository.getReservesByRoomId(roomId)
            withContext(Dispatchers.Main) { onSuccess(list) }
        }
    }

    fun getRoomById(id: Long): LiveData<RoomData> = mRepository.getRoomById(id)

}