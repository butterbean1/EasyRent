package ru.butterbean.easyrent.screens.room

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.butterbean.easyrent.database.repository.ReserveArchiveRepository
import ru.butterbean.easyrent.database.repository.ReserveRepository
import ru.butterbean.easyrent.database.repository.RoomRepository
import ru.butterbean.easyrent.models.ReserveData
import ru.butterbean.easyrent.models.RoomData
import ru.butterbean.easyrent.screens.room.item_models.ArchiveReserveModel
import ru.butterbean.easyrent.screens.room.item_models.CommonReserveModel
import ru.butterbean.easyrent.utils.APP_DATABASE
import ru.butterbean.easyrent.utils.AUTO_CHECK_IN_CHECK_OUT

class RoomViewModel(application: Application) : AndroidViewModel(application) {
    private val mRepository = ReserveRepository(APP_DATABASE.reserveDao())
    private val mRoomRepository = RoomRepository(APP_DATABASE.roomDao())
    private val mReserveArchiveRepository = ReserveArchiveRepository()

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

    fun getReservesArchiveCount(roomId: Long, onSuccess: (Long) -> Unit) {

        viewModelScope.launch(Dispatchers.IO) {
            val count = mReserveArchiveRepository.getReservesArchiveCount(roomId)
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
            if (AUTO_CHECK_IN_CHECK_OUT) mRepository.setAutoCheckInCheckOutForRoom(roomId)

            val list = mutableListOf<CommonReserveModel>()
            list.addAll(
                if (dontShowFreeReserves) mRepository.getAllReservesByRoomId(roomId)
                else mRepository.getReservesByRoomId(roomId)
            )
            val archivedCount = mReserveArchiveRepository.getReservesArchiveCount(roomId)
            if (archivedCount > 0) list.add(ArchiveReserveModel(roomId, archivedCount))

            withContext(Dispatchers.Main) { onSuccess(list) }
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


    fun replaceReserveToArchive(reserve: ReserveData, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            mReserveArchiveRepository.replaceReserveToArchive(reserve)
            withContext(Dispatchers.Main) {
                onSuccess()
            }
        }
    }

}