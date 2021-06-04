package ru.butterbean.easyrent.database.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.butterbean.easyrent.database.repository.GuestRepository
import ru.butterbean.easyrent.database.models.GuestData
import ru.butterbean.easyrent.utils.APP_DATABASE

class GuestViewModel(application: Application):AndroidViewModel(application) {
    val readAllGuests: LiveData<List<GuestData>>
    lateinit var currentGuest: GuestData

    private val repository: GuestRepository

    init {
        val guestDao = APP_DATABASE.guestDao()
        repository = GuestRepository(guestDao)
        readAllGuests = repository.readAllGuests
    }

    fun getById(id:Int):GuestData{
        return repository.getById(id)
    }
    fun addGuest(guest:GuestData){
        viewModelScope.launch(Dispatchers.IO) {
            val newId = repository.addGuest(guest)
            currentGuest = guest
            currentGuest.id = newId
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