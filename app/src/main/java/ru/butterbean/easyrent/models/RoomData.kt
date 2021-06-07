package ru.butterbean.easyrent.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.butterbean.easyrent.database.TABLE_ROOMS_NAME
import java.io.Serializable

@Entity(tableName = TABLE_ROOMS_NAME)
data class RoomData(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String = "",
    val address: String = "",
    var status: String = ""
) : Serializable
