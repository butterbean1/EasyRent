package ru.butterbean.easyrent.database.repository

import androidx.lifecycle.LiveData
import ru.butterbean.easyrent.database.dao.GuestDao
import ru.butterbean.easyrent.database.models.GuestData

class GuestRepository(private val guestDao: GuestDao) {

    val readAllGuests:LiveData<List<GuestData>> = guestDao.readAllGuests()

    suspend fun addGuest(guest:GuestData):Int{
        return guestDao.addGuest(guest).toInt()
    }
    fun getById(id:Int):GuestData{
        return guestDao.getById(id)
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