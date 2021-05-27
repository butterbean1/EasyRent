package ru.butterbean.easyrent.models

import android.os.Parcelable
import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "rooms_table")
data class RoomData(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val name:String,
    val address:String,
    val status:String
):Parcelable
