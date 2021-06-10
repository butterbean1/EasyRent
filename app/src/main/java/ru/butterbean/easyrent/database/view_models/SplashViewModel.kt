package ru.butterbean.easyrent.database.view_models

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import ru.butterbean.easyrent.database.repository.RoomRepository
import ru.butterbean.easyrent.models.RoomData
import ru.butterbean.easyrent.utils.APP_DATABASE

class SplashViewModel(application: Application):AndroidViewModel(application) {
    private val mRepository: RoomRepository = RoomRepository(APP_DATABASE.roomDao())

    fun getAllRooms():LiveData<List<RoomData>> = mRepository.readAllRooms

}