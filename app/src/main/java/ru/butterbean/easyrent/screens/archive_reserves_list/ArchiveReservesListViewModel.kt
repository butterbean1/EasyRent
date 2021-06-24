package ru.butterbean.easyrent.screens.archive_reserves_list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.butterbean.easyrent.database.repository.ReserveArchiveRepository
import ru.butterbean.easyrent.models.ReserveArchiveData
import ru.butterbean.easyrent.models.RoomData
import ru.butterbean.easyrent.utils.APP_DATABASE

class ArchiveReservesListViewModel(application: Application):AndroidViewModel(application) {
    private val mRepository: ReserveArchiveRepository = ReserveArchiveRepository(APP_DATABASE.reserveArchiveDao())

    fun deleteReserves(reserves:List<ReserveArchiveData>,onSuccess:()->Unit) {
        viewModelScope.launch {
            mRepository.deleteArchiveReserves(reserves)
            withContext(Dispatchers.Main){
                onSuccess()
            }
        }
    }

    fun getReservesByRoomId(roomId: Long):LiveData<List<ReserveArchiveData>>  = mRepository.getArchiveReservesByRoomId(roomId)

    fun getRoomById(id: Long): LiveData<RoomData> = mRepository.getRoomById(id)

    fun replaceReservesFromArchive(reserves: List<ReserveArchiveData>, onSuccess: () -> Unit) {
        viewModelScope.launch {
            mRepository.replaceReservesFromArchive(reserves)
            withContext(Dispatchers.Main){
                onSuccess()
            }
        }
    }


}