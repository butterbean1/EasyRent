package ru.butterbean.easyrent.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.butterbean.easyrent.database.TABLE_COSTS_NAME
import ru.butterbean.easyrent.database.TABLE_COST_ITEMS_NAME
import ru.butterbean.easyrent.database.TABLE_RESERVES_ARCHIVE_NAME
import ru.butterbean.easyrent.database.TABLE_RESERVES_NAME
import ru.butterbean.easyrent.models.ReserveData
import ru.butterbean.easyrent.models.StatisticQueryResult

@Dao
interface ReserveDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addReserve(reserve: ReserveData): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addReserves(reserves: List<ReserveData>)

    @Update
    suspend fun updateReserve(reserve: ReserveData)

    @Update
    suspend fun updateReserves(reserve: List<ReserveData>)

    @Delete
    suspend fun deleteReserve(reserve: ReserveData)

    @Delete
    suspend fun deleteReserves(reserves: List<ReserveData>)

    @Query("SELECT * FROM $TABLE_RESERVES_NAME WHERE (date('now','start of day')<=date(dateCheckOut,'start of day')) & (NOT wasCheckOut) ORDER BY dateCheckIn,dateCheckOut ASC")
    fun getAllActualReserves(): List<ReserveData>

    @Query("SELECT * FROM  $TABLE_RESERVES_NAME WHERE (date('now','start of day')>date(dateCheckOut,'start of day','+'||:analyseDepth||' days'))&(wasCheckOut=1) ORDER BY dateCheckOut DESC")
    fun getAllCheckOutedReserves(analyseDepth: Int): List<ReserveData>

    @Query("SELECT * FROM $TABLE_RESERVES_NAME WHERE id = :id")
    fun getReserveById(id: Long): ReserveData

    @Query("SELECT * FROM $TABLE_RESERVES_NAME ORDER BY dateCheckIn ASC")
    fun readAllReserves(): LiveData<List<ReserveData>>

    @Query("SELECT COUNT(*) FROM $TABLE_RESERVES_NAME WHERE roomId = :roomId")
    fun getReservesCount(roomId: Long): LiveData<Int>

    @Query("SELECT * FROM  $TABLE_RESERVES_NAME WHERE roomId= :roomId ORDER BY wasCheckOut,CASE WHEN (wasCheckOut=0) THEN (strftime('%s','now') + strftime('%s',dateCheckIn)) ELSE (strftime('%s','now') - strftime('%s',dateCheckOut)) END")
    fun getReservesByRoomId(roomId: Long): List<ReserveData>

    @Query("SELECT * FROM $TABLE_RESERVES_NAME WHERE (date('now','start of day')<=date(dateCheckOut,'start of day')) & (NOT wasCheckOut) & (roomId= :roomId) ORDER BY dateCheckIn,dateCheckOut ASC")
    fun getActualReservesByRoomId(roomId: Long): List<ReserveData>

    @Query(
        "SELECT " +
                "SUM(sum) sum, " +
                "SUM(costs) costs, " +
                "SUM(payment) payment, " +
                "SUM(guestsCount) guestsCount, " +
                "round(SUM(daysCount)) daysCount, " +
                "SUM(reservesCount) reservesCount " +
                "FROM (" +
                "SELECT " +
                "0 costs, " +
                "sum sum, " +
                "payment payment, " +
                "guestsCount guestsCount, " +
                "julianday(dateCheckOut)-julianday(dateCheckIn) daysCount, " +
                "1 reservesCount " +
                "FROM $TABLE_RESERVES_NAME " +
                "WHERE roomId = :roomId AND " +
                "dateCheckOut BETWEEN :startDate AND :endDate " +
                "UNION ALL " +
                "SELECT " +
                "0 costs, " +
                "sum sum, " +
                "payment payment, " +
                "guestsCount guestsCount, " +
                "julianday(dateCheckOut)-julianday(dateCheckIn) daysCount, " +
                "1 reservesCount " +
                "FROM $TABLE_RESERVES_ARCHIVE_NAME " +
                "WHERE roomId = :roomId AND " +
                "dateCheckOut BETWEEN :startDate AND :endDate " +
                "UNION ALL " +
                "SELECT " +
                "sum costs, " +
                "0 sum, " +
                "0 payment, " +
                "0 guestsCount, " +
                "0 daysCount, " +
                "0 reservesCount " +
                "FROM $TABLE_COSTS_NAME " +
                "WHERE roomId = :roomId AND " +
                "date BETWEEN :startDate AND :endDate" +
                ")"
    )
    fun getStatistic(roomId: Long, startDate: String, endDate: String): StatisticQueryResult


}
