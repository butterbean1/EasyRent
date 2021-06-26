package ru.butterbean.easyrent.screens.splash

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.butterbean.easyrent.database.repository.ReserveArchiveRepository
import ru.butterbean.easyrent.database.repository.ReserveRepository
import ru.butterbean.easyrent.database.repository.RoomRepository
import ru.butterbean.easyrent.models.RoomData
import ru.butterbean.easyrent.utils.APP_DATABASE

class SplashViewModel(application: Application) : AndroidViewModel(application) {
    private val mRepository = ReserveArchiveRepository()
    private val mReserveRepository = ReserveRepository(APP_DATABASE.reserveDao())
    private val mRoomRepository = RoomRepository(APP_DATABASE.roomDao())

    fun getAllRooms(onSuccess: (List<RoomData>) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val rooms = mRoomRepository.getAllRooms()
            withContext(Dispatchers.Main) {
                onSuccess(rooms)
            }
        }
    }

    fun replaceReservesToArchive(analyseDepth: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.replaceReservesToArchive(analyseDepth)
        }
    }

    fun setAutoCheckInCheckOut() {
        viewModelScope.launch(Dispatchers.IO) {
            mReserveRepository.setAutoCheckInCheckOut()
        }
    }

}