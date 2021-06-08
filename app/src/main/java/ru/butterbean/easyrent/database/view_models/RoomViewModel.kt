package ru.butterbean.easyrent.database.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.butterbean.easyrent.database.repository.ReserveRepository
import ru.butterbean.easyrent.models.ReserveData
import ru.butterbean.easyrent.models.RoomData
import ru.butterbean.easyrent.utils.APP_DATABASE

class RoomViewModel(application: Application):AndroidViewModel(application) {
    private val mRepository: ReserveRepository = ReserveRepository(APP_DATABASE.reserveDao())

    fun deleteReserve(reserve: ReserveData) {
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.deleteReserve(reserve)
            mRepository.updateRoomStatus(reserve.roomId)
        }
    }

    fun getReservesCount(roomId:Long): LiveData<Int> = mRepository.getReservesCount(roomId)

    fun getReservesByRoomId(roomId:Long): LiveData<List<ReserveData>> = mRepository.getReservesByRoomId(roomId)

    fun getStatus(roomId:Long): LiveData<String> = mRepository.getStatus(roomId)

    fun getRoomById(id:Long):LiveData<RoomData> = mRepository.getRoomById(id)


}