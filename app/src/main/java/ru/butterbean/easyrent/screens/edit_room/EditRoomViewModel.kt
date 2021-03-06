package ru.butterbean.easyrent.screens.edit_room

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.butterbean.easyrent.database.repository.RoomRepository
import ru.butterbean.easyrent.models.RoomData
import ru.butterbean.easyrent.utils.APP_DATABASE
import ru.butterbean.easyrent.utils.ONLY_ONE_ROOM

class EditRoomViewModel(application: Application) : AndroidViewModel(application) {
    private val mRepository = RoomRepository(APP_DATABASE.roomDao())

    fun addRoom(room: RoomData,onSuccess:(newId:Long) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val newId = mRepository.addRoom(room)
            withContext(Dispatchers.Main){onSuccess(newId)}
        }
    }

    fun updateRoom(room: RoomData,onSuccess:() -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.updateRoom(room)
            withContext(Dispatchers.Main){onSuccess()}
        }
    }

}