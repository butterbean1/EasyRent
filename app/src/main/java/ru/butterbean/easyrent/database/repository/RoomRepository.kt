package ru.butterbean.easyrent.database.repository

import androidx.lifecycle.LiveData
import ru.butterbean.easyrent.database.dao.RoomDao
import ru.butterbean.easyrent.models.RoomData
import ru.butterbean.easyrent.utils.ONLY_ONE_ROOM

class RoomRepository(private val roomDao: RoomDao) {

    val readAllRooms: LiveData<List<RoomData>> = roomDao.readAllRooms()

    suspend fun addRoom(room: RoomData): Long {
        val newId = roomDao.addRoom(room)
        ONLY_ONE_ROOM = roomDao.getRoomsCount() == 1
        return newId
    }

    suspend fun deleteRoom(room: RoomData) {
        roomDao.deleteRoom(room)
    }

    suspend fun updateRoom(room: RoomData) {
        roomDao.updateRoom(room)
    }

    fun getAllRooms(): List<RoomData> = roomDao.getAllRooms()

    fun getRoomById(id: Long): RoomData = roomDao.getRoomById(id)

}