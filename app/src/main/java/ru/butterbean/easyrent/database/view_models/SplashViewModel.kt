package ru.butterbean.easyrent.database.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.butterbean.easyrent.database.repository.ReserveArchiveRepository
import ru.butterbean.easyrent.models.RoomData
import ru.butterbean.easyrent.utils.APP_DATABASE

class SplashViewModel(application: Application) : AndroidViewModel(application) {
    private val mRepository = ReserveArchiveRepository(APP_DATABASE.reserveArchiveDao())

    fun getAllRooms(): LiveData<List<RoomData>> = mRepository.readAllRooms()

    fun replaceReservesToArchive(analyseDepth: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.replaceReservesToArchive(analyseDepth)
        }
    }

}