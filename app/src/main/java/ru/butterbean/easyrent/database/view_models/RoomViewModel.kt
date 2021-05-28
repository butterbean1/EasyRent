package ru.butterbean.easyrent.database.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.butterbean.easyrent.database.MainDatabase
import ru.butterbean.easyrent.database.repository.RoomRepository
import ru.butterbean.easyrent.models.RoomData

class RoomViewModel(application: Application):AndroidViewModel(application) {
    val readAllRooms: LiveData<List<RoomData>>
    private val repository: RoomRepository

    init {
        val roomDao = MainDatabase.getDatabase(application).roomDao()
        repository = RoomRepository(roomDao)
        readAllRooms = repository.readAllRooms
    }

    fun addRoom(room:RoomData){
        viewModelScope.launch(Dispatchers.IO) { repository.addRoom(room) }
    }
    fun deleteRoom(room:RoomData){
        viewModelScope.launch(Dispatchers.IO) { repository.deleteRoom(room) }
    }
    fun updateRoom(room:RoomData){
        viewModelScope.launch(Dispatchers.IO) { repository.updateRoom(room) }
    }
    fun deleteAllRooms(){
        viewModelScope.launch(Dispatchers.IO) { repository.deleteAllRooms() }
    }

}