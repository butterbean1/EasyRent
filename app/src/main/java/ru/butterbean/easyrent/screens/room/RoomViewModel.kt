package ru.butterbean.easyrent.screens.room

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
import ru.butterbean.easyrent.screens.room.item_models.CommonReserveModel
import ru.butterbean.easyrent.utils.APP_DATABASE
import ru.butterbean.easyrent.utils.AUTO_CHECK_IN_CHECK_OUT

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

    fun getReservesArchiveCount(roomId: Long, onSuccess: (Int) -> Unit) {

        viewModelScope.launch(Dispatchers.IO) {
            val count = mRepository.getReservesArchiveCount(roomId)
            withContext(Dispatchers.Main) {
                onSuccess(count)
            }
        }
    }

    fun getReservesByRoomId(
        roomId: Long,
        dontShowFreeReserves: Boolean,
        onSuccess: (List<CommonReserveModel>) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            // сначала установим авто-отметки заселения/выселения
            if (AUTO_CHECK_IN_CHECK_OUT) mRepository.setAutoCheckInCheckOut(roomId)

            val list: List<CommonReserveModel> =
                if (dontShowFreeReserves) mRepository.getAllReservesByRoomId(roomId)
                else mRepository.getReservesByRoomId(roomId)
            withContext(Dispatchers.Main) { onSuccess(list) }
        }
    }

    fun getRoomById(id: Long): LiveData<RoomData> = mRepository.getRoomById(id)

    fun replaceReserveToArchive(reserve: ReserveData, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.replaceReserveToArchive(reserve)
            withContext(Dispatchers.Main) {
                onSuccess()
            }
        }
    }

}