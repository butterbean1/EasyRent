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
    val roomId:Int,
    val guestId:Int,
    val guestsCount:Int,
    val sum:Int,
    val payment:Int,
    val dateCheckIn:String,
    val dateCheckOut:String,
    val wasCheckIn:Boolean,
    val wasCheckOut:Boolean
):Parcelable
