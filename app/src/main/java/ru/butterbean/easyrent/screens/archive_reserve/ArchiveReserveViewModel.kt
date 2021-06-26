package ru.butterbean.easyrent.screens.archive_reserve

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

class ArchiveReserveViewModel(application: Application) : AndroidViewModel(application) {
    private val mRepository = ReserveArchiveRepository()
    private val mReserveRepository = ReserveRepository(APP_DATABASE.reserveDao())
    private val mRoomRepository = RoomRepository(APP_DATABASE.roomDao())

    fun replaceReserveFromArchive(
        reserve: ReserveArchiveData,
        onSuccess: (ReserveData) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val newId = mRepository.replaceReserveFromArchive(reserve)
            mReserveRepository.updateRoomStatus(reserve.roomId)
            val newReserve = mReserveRepository.getReserveById(newId)
            withContext(Dispatchers.Main) {
                onSuccess(newReserve)
            }
        }
    }

     fun deleteReserveArchive(reserve: ReserveArchiveData, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.deleteArchiveReserve(reserve)
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