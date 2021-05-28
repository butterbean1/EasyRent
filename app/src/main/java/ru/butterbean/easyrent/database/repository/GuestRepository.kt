package ru.butterbean.easyrent.database.repository

import androidx.lifecycle.LiveData
import ru.butterbean.easyrent.database.dao.GuestDao
import ru.butterbean.easyrent.models.GuestData

class GuestRepository(private val guestDao: GuestDao) {

    val readAllGuests:LiveData<List<GuestData>> = guestDao.readAllGuests()

    suspend fun addGuest(guest:GuestData){
        guestDao.addGuest(guest)
    }
    suspend fun deleteGuest(guest:GuestData){
        guestDao.deleteGuest(guest)
    }
    suspend fun updateGuest(guest:GuestData){
        guestDao.updateGuest(guest)
    }
    suspend fun deleteAllGuests(){
        guestDao.deleteAllGuests()
    }

}