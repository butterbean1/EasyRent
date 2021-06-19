package ru.butterbean.easyrent.screens

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

class RoomDialogViewModel(application: Application) : AndroidViewModel(application) {
    private val mRepository = RoomRepository(APP_DATABASE.roomDao())

    fun getReservesCount(roomId: Long): LiveData<Int> = mRepository.getReservesCount(roomId)

    fun deleteRoom(room: RoomData, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.deleteRoom(room)
            withContext(Dispatchers.Main) { onSuccess() }
        }
    }


}