package ru.butterbean.easyrent.database.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.butterbean.easyrent.database.CURRENT_ROOM
import ru.butterbean.easyrent.database.repository.RoomRepository
import ru.butterbean.easyrent.models.RoomData
import ru.butterbean.easyrent.utils.APP_DATABASE
import ru.butterbean.easyrent.utils.getEmptyRoom

class RoomViewModel(application: Application) : AndroidViewModel(application) {
    val readAllRooms: LiveData<List<RoomData>>
    lateinit var currentRoom:RoomData
    private val repository: RoomRepository

    init {
        val roomDao = APP_DATABASE.roomDao()
        repository = RoomRepository(roomDao)
        readAllRooms = repository.readAllRooms
    }

    fun getReservesCount(roomId:Int): LiveData<Int> = repository.getReservesCount(roomId)


    fun addRoom(room: RoomData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addRoom(room)
            currentRoom = room
        }
    }

    fun deleteRoom(room: RoomData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteRoom(room)
            currentRoom = getEmptyRoom()
        }
    }

    fun updateRoom(room: RoomData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateRoom(room)
            currentRoom = room
        }
    }

    fun deleteAllRooms() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllRooms()
            currentRoom = getEmptyRoom()
        }
    }
}