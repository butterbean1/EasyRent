package ru.butterbean.easyrent.database.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import ru.butterbean.easyrent.database.TABLE_ROOMS_NAME

@Entity(tableName = TABLE_ROOMS_NAME)
data class RoomData(
    @PrimaryKey(autoGenerate = true)
    var id:Long,
    val name:String = "",
    val address:String = "",
    var status:String = ""
)
