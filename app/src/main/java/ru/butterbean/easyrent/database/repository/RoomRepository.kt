package ru.butterbean.easyrent.database.repository

import androidx.lifecycle.LiveData
import ru.butterbean.easyrent.database.dao.RoomDao
import ru.butterbean.easyrent.database.models.RoomData

class RoomRepository(private val roomDao: RoomDao) {

    val readAllRooms:LiveData<List<RoomData>> = roomDao.readAllRooms()

    suspend fun addRoom(room:RoomData):Int{
        return roomDao.addRoom(room).toInt()
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

    fun getReservesCount(roomId:Int):LiveData<Int> = roomDao.getReservesCount(roomId)
    fun getById(id:Int):LiveData<RoomData> = roomDao.getById(id)
}