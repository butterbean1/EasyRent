package ru.butterbean.easyrent.screens.archive_reserves_list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.butterbean.easyrent.database.repository.ReserveArchiveExtFileRepository
import ru.butterbean.easyrent.database.repository.ReserveArchiveRepository
import ru.butterbean.easyrent.database.repository.ReserveExtFileRepository
import ru.butterbean.easyrent.database.repository.RoomRepository
import ru.butterbean.easyrent.models.ReserveArchiveData
import ru.butterbean.easyrent.models.RoomData
import ru.butterbean.easyrent.utils.APP_DATABASE
import ru.butterbean.easyrent.utils.deleteLocalFiles

class ArchiveReservesListViewModel(application: Application) : AndroidViewModel(application) {
    private val mRepository = ReserveArchiveRepository()
    private val mRoomRepository = RoomRepository(APP_DATABASE.roomDao())
    private val mReserveArchiveExtFileRepository = ReserveArchiveExtFileRepository(APP_DATABASE.reserveArchiveExtFilesDao())
    private val mReserveExtFileRepository = ReserveExtFileRepository()

    fun deleteArchiveReserves(reserves: List<ReserveArchiveData>, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            reserves.forEach {
                deleteLocalFiles(mReserveArchiveExtFileRepository.getExtFileDirsByReserveId(it.id))
            }
            mRepository.deleteArchiveReserves(reserves)
            withContext(Dispatchers.Main) {
                onSuccess()
            }
        }
    }

    fun getArchiveReservesByRoomId(roomId: Long): LiveData<List<ReserveArchiveData>> =
        mRepository.getArchiveReservesByRoomId(roomId)

    fun getRoomById(id: Long, onSuccess: (RoomData) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val room = mRoomRepository.getRoomById(id)
            withContext(Dispatchers.Main) {
                onSuccess(room)
            }
        }
    }

    fun replaceReservesFromArchive(reserves: List<ReserveArchiveData>, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            reserves.forEach {reserve ->
                val newId = mRepository.replaceReserveFromArchive(reserve)
                mReserveExtFileRepository.replaceExtFilesFromArchive(reserve.id,newId)
            }
            mRepository.deleteArchiveReserves(reserves)
            withContext(Dispatchers.Main) {
                onSuccess()
            }
        }
    }


}