package ru.butterbean.easyrent.screens.edit_reserve

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.butterbean.easyrent.database.repository.*
import ru.butterbean.easyrent.models.*
import ru.butterbean.easyrent.utils.APP_DATABASE

class EditReserveViewModel(application: Application) : AndroidViewModel(application) {
    private val mRepository = ReserveRepository(APP_DATABASE.reserveDao())
    private val mRoomRepository = RoomRepository(APP_DATABASE.roomDao())
    private val mReserveArchiveRepository = ReserveArchiveRepository()
    private val mReserveExtFileRepository = ReserveExtFileRepository(APP_DATABASE.reserveExtFileDao())
    private val mReserveArchiveExtFileRepository = ReserveArchiveExtFileRepository(APP_DATABASE.reserveArchiveExtFilesDao())

    fun addReserve(reserve: ReserveData, onSuccess: (Long) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val newId = mRepository.addReserve(reserve)
            mRepository.updateRoomStatus(reserve.roomId)
            withContext(Dispatchers.Main) {
                onSuccess(newId)
            }
        }
    }

    fun addReserveArchive(reserve: ReserveArchiveData, onSuccess: (Long) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val newId = mReserveArchiveRepository.addReserveArchive(reserve)
            withContext(Dispatchers.Main) {
                onSuccess(newId)
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

    fun getExtFilesCount(reserveId: Long, onSuccess: (Int) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val count = mReserveExtFileRepository.getExtFilesCount(reserveId)
            withContext(Dispatchers.Main) {
                onSuccess(count)
            }
        }
    }

    fun getSingleExtFileByReserveId(reserveId: Long, onSuccess: (ReserveExtFileData) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val extFile = mReserveExtFileRepository.getSingleExtFileByReserveId(reserveId)
            withContext(Dispatchers.Main) {
                onSuccess(extFile)
            }
        }
    }

    fun addReserveExtFile(extFile: ReserveExtFileData, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            mReserveExtFileRepository.addReserveExtFile(extFile)
            withContext(Dispatchers.Main) {
                onSuccess()
            }
        }
    }

    fun addReserveExtFiles(extFiles: List<ReserveExtFileData>) {
        viewModelScope.launch(Dispatchers.IO) {
            mReserveExtFileRepository.addReserveExtFiles(extFiles)
        }
    }

    fun addReserveArchiveExtFiles(extFiles: List<ReserveArchiveExtFileData>) {
        viewModelScope.launch(Dispatchers.IO) {
            mReserveArchiveExtFileRepository.addReserveExtFiles(extFiles)
        }
    }


}