package ru.butterbean.easyrent.database.models

import androidx.room.*
import ru.butterbean.easyrent.database.TABLE_RESERVES_NAME

@Entity(tableName = TABLE_RESERVES_NAME,foreignKeys = [ForeignKey(entity = RoomData::class,parentColumns = ["id"],childColumns = ["roomId"],onDelete = ForeignKey.CASCADE)])
data class ReserveData(
    @PrimaryKey(autoGenerate = true)
    var id:Long,
    @ColumnInfo(index = true)
    val roomId:Long = 0,
    val guestName:String = "",
    val guestsCount:Int = 0,
    val sum:Int = 0,
    val payment:Int = 0,
    val dateCheckIn:String = "",
    val dateCheckOut:String = "",
    val wasCheckIn:Boolean = false,
    val wasCheckOut:Boolean = false
)
