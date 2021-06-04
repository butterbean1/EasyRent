package ru.butterbean.easyrent.database.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import ru.butterbean.easyrent.database.TABLE_GUESTS_NAME

@Parcelize
@Entity(tableName = TABLE_GUESTS_NAME)
data class GuestData(
    @PrimaryKey(autoGenerate = true)
    var id:Int,
    val name:String = "",
    val fullname:String = "",
    val document:String = ""
):Parcelable
