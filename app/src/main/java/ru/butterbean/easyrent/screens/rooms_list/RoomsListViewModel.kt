package ru.butterbean.easyrent.screens.rooms_list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import ru.butterbean.easyrent.database.repository.RoomRepository
import ru.butterbean.easyrent.models.RoomData
import ru.butterbean.easyrent.utils.APP_DATABASE

class RoomsListViewModel(application:Application):AndroidViewModel(application) {
    val readAllRooms: LiveData<List<RoomData>>

    init {
        val rep = RoomRepository(APP_DATABASE.roomDao())
        readAllRooms = rep.readAllRooms
    }

}