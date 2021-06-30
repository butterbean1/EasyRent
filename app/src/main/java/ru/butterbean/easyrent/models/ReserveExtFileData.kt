package ru.butterbean.easyrent.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import ru.butterbean.easyrent.database.TABLE_RESERVES_EXT_FILES_NAME
import java.io.Serializable

@Entity(
    tableName = TABLE_RESERVES_EXT_FILES_NAME,
    foreignKeys = [ForeignKey(
        entity = ReserveData::class,
        parentColumns = ["id"],
        childColumns = ["reserveId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class ReserveExtFileData(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(index = true)
    val reserveId: Long,
    val dirName: String = "",
    val fileName: String = "",
    val fileType: String = "",
    val isImage: Boolean = false
) : Serializable