package ru.butterbean.easyrent.models

import androidx.recyclerview.widget.RecyclerView
import androidx.room.*
import ru.butterbean.easyrent.database.TABLE_RESERVES_NAME
import ru.butterbean.easyrent.screens.reserves.ReserveType
import java.io.Serializable

@Entity(tableName = TABLE_RESERVES_NAME,foreignKeys = [ForeignKey(entity = RoomData::class,parentColumns = ["id"],childColumns = ["roomId"],onDelete = ForeignKey.CASCADE)])
data class ReserveData(
    @PrimaryKey(autoGenerate = true)
    val id:Long,
    @ColumnInfo(index = true)
    val roomId:Long,
    val guestName:String = "",
    val guestsCount:Int = 0,
    val sum:Int = 0,
    val payment:Int = 0,
    val dateCheckIn:String = "",
    val dateCheckOut:String = "",
    val wasCheckIn:Boolean = false,
    val wasCheckOut:Boolean = false
):Serializable
