package ru.butterbean.easyrent.screens.archive_reserve

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.butterbean.easyrent.database.repository.*
import ru.butterbean.easyrent.models.*
import ru.butterbean.easyrent.utils.APP_DATABASE
import ru.butterbean.easyrent.utils.deleteLocalFiles

class ArchiveReserveViewModel(application: Application) : AndroidViewModel(application) {
    private val mRepository = ReserveArchiveRepository()
    private val mReserveRepository = ReserveRepository(APP_DATABASE.reserveDao())
    private val mRoomRepository = RoomRepository(APP_DATABASE.roomDao())
    private val mReserveExtFileRepository = ReserveExtFileRepository()
    private val mReserveArchiveExtFileRepository = ReserveArchiveExtFileRepository(APP_DATABASE.reserveArchiveExtFilesDao())

    fun replaceReserveFromArchive(
        reserve: ReserveArchiveData,
        onSuccess: (ReserveData) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val newId = mRepository.replaceReserveFromArchive(reserve)
            mReserveExtFileRepository.replaceExtFilesFromArchive(reserve.id,newId)
            mRepository.deleteArchiveReserve(reserve)
            mReserveRepository.updateRoomStatus(reserve.roomId)
            val newReserve = mReserveRepository.getReserveById(newId)
            withContext(Dispatchers.Main) {
                onSuccess(newReserve)
            }
        }
    }

     fun deleteReserveArchive(reserve: ReserveArchiveData, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteLocalFiles(mReserveArchiveExtFileRepository.getExtFileDirsByReserveId(reserve.id))
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

    fun getExtFilesCount(reserveId: Long, onSuccess: (Int) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val count = mReserveArchiveExtFileRepository.getExtFilesCount(reserveId)
            withContext(Dispatchers.Main) {
                onSuccess(count)
            }
        }
    }

    fun getSingleExtFileByReserveId(reserveId: Long, onSuccess: (ReserveArchiveExtFileData) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val extFile = mReserveArchiveExtFileRepository.getSingleExtFileByReserveId(reserveId)
            withContext(Dispatchers.Main) {
                onSuccess(extFile)
            }
        }
    }


}