package ru.butterbean.easyrent.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.butterbean.easyrent.database.TABLE_GUESTS_NAME
import java.io.Serializable

@Entity(tableName = TABLE_GUESTS_NAME)
data class GuestData(
    @PrimaryKey(autoGenerate = true)
    val id:Long,
    val name:String = "",
    val fullname:String = "",
    val document:String = ""
):Serializable
