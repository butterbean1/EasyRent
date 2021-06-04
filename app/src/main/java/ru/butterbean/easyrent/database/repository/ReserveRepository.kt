package ru.butterbean.easyrent.database.repository

import androidx.lifecycle.LiveData
import ru.butterbean.easyrent.database.dao.ReserveDao
import ru.butterbean.easyrent.database.models.ReserveData

class ReserveRepository(private val reserveDao: ReserveDao) {

    val readAllReserves:LiveData<List<ReserveData>> = reserveDao.readAllReserves()

    suspend fun addReserve(reserve:ReserveData):Long{
        return reserveDao.addReserve(reserve)
    }
    suspend fun deleteReserve(reserve:ReserveData){
        reserveDao.deleteReserve(reserve)
    }
    suspend fun updateReserve(reserve:ReserveData){
        reserveDao.updateReserve(reserve)
    }
    suspend fun updateRoomStatus(reserve:ReserveData){
        reserveDao.updateReserve(reserve)
    }
    suspend fun deleteAllReserves(){
        reserveDao.deleteAllReserves()
    }

    fun getReservesByRoomId(roomId: Long): LiveData<List<ReserveData>> = reserveDao.getReservesByRoomId(roomId)

    fun getActualReservesByRoomId(roomId: Long): LiveData<List<ReserveData>> = reserveDao.getActualReservesByRoomId(roomId)

    fun getEqualseservesByRoomId(roomId: Long): LiveData<List<ReserveData>> = reserveDao.getEqualseservesByRoomId(roomId)

}