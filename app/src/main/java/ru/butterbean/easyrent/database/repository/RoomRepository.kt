package ru.butterbean.easyrent.database.repository

import androidx.lifecycle.LiveData
import ru.butterbean.easyrent.database.dao.RoomDao
import ru.butterbean.easyrent.models.RoomData

class RoomRepository(private val roomDao: RoomDao) {

    val readAllRooms:LiveData<List<RoomData>> = roomDao.readAllRooms()

    suspend fun addRoom(room:RoomData):Long{
       return roomDao.addRoom(room)
    }
    suspend fun deleteRoom(room:RoomData){
        roomDao.deleteRoom(room)
    }
    suspend fun updateRoom(room:RoomData){
        roomDao.updateRoom(room)
    }
    suspend fun deleteAllRooms(){
        roomDao.deleteAllRooms()
    }

    fun getReservesCount(roomId:Long):LiveData<Int> = roomDao.getReservesCount(roomId)

    fun getRoomsCount():LiveData<Int> = roomDao.getRoomsCount()
}