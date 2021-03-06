package ru.butterbean.easyrent.screens.guest

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    fun addGuest(guest:GuestData,onSuccess:(newId:Long)->Unit){
        viewModelScope.launch(Dispatchers.IO) {
            val newId = repository.addGuest(guest)
            withContext(Dispatchers.Main){onSuccess(newId)}
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