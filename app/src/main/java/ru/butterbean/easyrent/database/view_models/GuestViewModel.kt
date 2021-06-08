package ru.butterbean.easyrent.database.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.butterbean.easyrent.database.repository.GuestRepository
import ru.butterbean.easyrent.models.GuestData
import ru.butterbean.easyrent.utils.APP_DATABASE

class GuestViewModel(application: Application):AndroidViewModel(application) {
    val readAllGuests: LiveData<List<GuestData>>
    private val repository: GuestRepository

    init {
        val guestDao = APP_DATABASE.guestDao()
        repository = GuestRepository(guestDao)
        readAllGuests = repository.readAllGuests
    }

    fun getById(id:Long):GuestData{
        return repository.getById(id)
    }
    fun addGuest(guest:GuestData){
        viewModelScope.launch(Dispatchers.IO) {
            val newId = repository.addGuest(guest)
        }
    }
    fun deleteGuest(guest:GuestData){
        viewModelScope.launch(Dispatchers.IO) { repository.deleteGuest(guest) }
    }
    fun updateGuest(guest:GuestData){
        viewModelScope.launch(Dispatchers.IO) { repository.updateGuest(guest) }
    }
    fun deleteAllGuests(){
        viewModelScope.launch(Dispatchers.IO) { repository.deleteAllGuests() }
    }

}