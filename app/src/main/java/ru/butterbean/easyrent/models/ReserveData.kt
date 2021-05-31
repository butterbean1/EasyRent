package ru.butterbean.easyrent.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import ru.butterbean.easyrent.TABLE_RESERVES_NAME
import ru.butterbean.easyrent.TABLE_ROOMS_NAME

@Parcelize
@Entity(tableName = TABLE_RESERVES_NAME)
data class ReserveData(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val roomId:Int = 0,
    val guestId:Int = 0,
    val guestsCount:Int = 0,
    val sum:Int = 0,
    val payment:Int = 0,
    val dateCheckIn:String = "",
    val dateCheckOut:String = "",
    val wasCheckIn:Boolean = false,
    val wasCheckOut:Boolean = false
):Parcelable
