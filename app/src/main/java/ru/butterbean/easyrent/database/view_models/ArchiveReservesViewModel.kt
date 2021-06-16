package ru.butterbean.easyrent.database.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import ru.butterbean.easyrent.database.repository.ReserveArchiveRepository
import ru.butterbean.easyrent.models.ReserveArchiveData
import ru.butterbean.easyrent.models.RoomData
import ru.butterbean.easyrent.utils.APP_DATABASE

class ArchiveReservesViewModel(application: Application):AndroidViewModel(application) {
    private val mRepository: ReserveArchiveRepository = ReserveArchiveRepository(APP_DATABASE.reserveArchiveDao())

    fun getReservesByRoomId(roomId: Long):LiveData<List<ReserveArchiveData>>  = mRepository.getArchiveReservesByRoomId(roomId)

    fun getRoomById(id: Long): LiveData<RoomData> = mRepository.getRoomById(id)


}