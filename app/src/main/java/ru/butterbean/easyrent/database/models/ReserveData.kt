package ru.butterbean.easyrent.database.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import ru.butterbean.easyrent.database.TABLE_RESERVES_NAME

@Parcelize
@Entity(tableName = TABLE_RESERVES_NAME)
data class ReserveData(
    @PrimaryKey(autoGenerate = true)
    var id:Int,
    val roomId:Int = 0,
    val guestName:String = "",
    val guestsCount:Int = 0,
    val sum:Int = 0,
    val payment:Int = 0,
    val dateCheckIn:String = "",
    val dateCheckOut:String = "",
    val wasCheckIn:Boolean = false,
    val wasCheckOut:Boolean = false
):Parcelable
