package ru.butterbean.easyrent.database.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.butterbean.easyrent.database.repository.ReserveRepository
import ru.butterbean.easyrent.models.ReserveData
import ru.butterbean.easyrent.utils.APP_DATABASE

class EditReserveViewModel(application: Application) : AndroidViewModel(application) {
    private val mRepository: ReserveRepository

    init {
        val reserveDao = APP_DATABASE.reserveDao()
        mRepository = ReserveRepository(reserveDao)
    }

    fun addReserve(reserve: ReserveData, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.addReserve(reserve)
            mRepository.updateRoomStatus(reserve.roomId) {
                onSuccess()
            }
        }
    }

    fun deleteReserve(reserve: ReserveData, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.deleteReserve(reserve)
            mRepository.updateRoomStatus(reserve.roomId) {
                onSuccess()
            }
        }
    }

    fun updateReserve(reserve: ReserveData, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.updateReserve(reserve)
            mRepository.updateRoomStatus(reserve.roomId) {
                onSuccess()
            }
        }
    }
}