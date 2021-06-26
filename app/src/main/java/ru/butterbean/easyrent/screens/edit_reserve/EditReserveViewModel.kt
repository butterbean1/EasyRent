package ru.butterbean.easyrent.screens.edit_reserve

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.butterbean.easyrent.database.repository.ReserveArchiveRepository
import ru.butterbean.easyrent.database.repository.ReserveRepository
import ru.butterbean.easyrent.database.repository.RoomRepository
import ru.butterbean.easyrent.models.ReserveArchiveData
import ru.butterbean.easyrent.models.ReserveData
import ru.butterbean.easyrent.models.RoomData
import ru.butterbean.easyrent.utils.APP_DATABASE

class EditReserveViewModel(application: Application) : AndroidViewModel(application) {
    private val mRepository = ReserveRepository(APP_DATABASE.reserveDao())
    private val mRoomRepository = RoomRepository(APP_DATABASE.roomDao())
    private val mReserveArchiveRepository = ReserveArchiveRepository()

    fun addReserve(reserve: ReserveData, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.addReserve(reserve)
            mRepository.updateRoomStatus(reserve.roomId)
            withContext(Dispatchers.Main) {
                onSuccess()
            }
        }
    }

    fun addReserveArchive(reserve: ReserveArchiveData, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            mReserveArchiveRepository.addReserveArchive(reserve)
            withContext(Dispatchers.Main) {
                onSuccess()
            }
        }
    }

    fun replaceReserveToArchive(
        newReserve: ReserveArchiveData,
        oldReserve: ReserveData,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            mReserveArchiveRepository.addReserveArchive(newReserve)
            mRepository.deleteReserve(oldReserve)
            mRepository.updateRoomStatus(newReserve.roomId)
            withContext(Dispatchers.Main) {
                onSuccess()
            }
        }
    }

    fun deleteReserve(reserve: ReserveData, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.deleteReserve(reserve)
            mRepository.updateRoomStatus(reserve.roomId)
            withContext(Dispatchers.Main) {
                onSuccess()
            }
        }
    }

    fun updateReserve(reserve: ReserveData, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.updateReserve(reserve)
            mRepository.updateRoomStatus(reserve.roomId)
            withContext(Dispatchers.Main) {
                onSuccess()
            }
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