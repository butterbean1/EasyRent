package ru.butterbean.easyrent.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import ru.butterbean.easyrent.database.TABLE_COSTS_NAME
import java.io.Serializable

@Entity(
    tableName = TABLE_COSTS_NAME,
    foreignKeys = [
        ForeignKey(
            entity = RoomData::class,
            parentColumns = ["id"],
            childColumns = ["roomId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CostItemData::class,
            parentColumns = ["id"],
            childColumns = ["costItemId"],
            onDelete = ForeignKey.RESTRICT
        )
    ]
)
data class CostData(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(index = true)
    val roomId: Long,
    @ColumnInfo(index = true)
    val costItemId: Long = 0,
    val sum: Int = 0,
    val date: String = "",
    val description: String = ""
) : Serializable