package ru.butterbean.easyrent.database.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.butterbean.easyrent.database.repository.ReserveRepository
import ru.butterbean.easyrent.models.ReserveData
import ru.butterbean.easyrent.utils.APP_DATABASE

class ReserveViewModel(application: Application) : AndroidViewModel(application) {
    val readAllReserves: LiveData<List<ReserveData>>
    lateinit var currentReserve: ReserveData
    private val repository: ReserveRepository

    init {
        val reserveDao = APP_DATABASE.reserveDao()
        repository = ReserveRepository(reserveDao)
        readAllReserves = repository.readAllReserves
    }

    fun getReservesByRoomId(roomId:Int):LiveData<List<ReserveData>> = repository.getReservesByRoomId(roomId)

    fun addReserve(reserve: ReserveData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addReserve(reserve)
            currentReserve = reserve
        }
    }

    fun deleteReserve(reserve: ReserveData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteReserve(reserve)
        }
    }

    fun updateReserve(reserve: ReserveData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateReserve(reserve)
            currentReserve = reserve
        }
    }

    fun deleteAllReserves() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllReserves()
        }
    }

}