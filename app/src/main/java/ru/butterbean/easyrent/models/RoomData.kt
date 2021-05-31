package ru.butterbean.easyrent.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import ru.butterbean.easyrent.TABLE_ROOMS_NAME

@Parcelize
@Entity(tableName = TABLE_ROOMS_NAME)
data class RoomData(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val name:String = "",
    val address:String = "",
    val status:String = ""
):Parcelable
