package ru.butterbean.easyrent.models

import android.net.Uri
import android.os.Bundle
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import ru.butterbean.easyrent.database.TABLE_RESERVES_ARCHIVE_EXT_FILES_NAME
import ru.butterbean.easyrent.utils.APP_ACTIVITY
import java.io.File
import java.io.Serializable

@Entity(
    tableName = TABLE_RESERVES_ARCHIVE_EXT_FILES_NAME,
    foreignKeys = [ForeignKey(
        entity = ReserveArchiveData::class,
        parentColumns = ["id"],
        childColumns = ["reserveId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class ReserveArchiveExtFileData(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(index = true)
    val reserveId: Long,
    val dirName: String = "",
    val fileName: String = "",
    val fileType: String = "",
    val isImage: Boolean = false
) : Serializable {
    fun getParamsBundle(): Bundle {
        val res = Bundle()
        val file = File(
            APP_ACTIVITY.filesDir.path + "/" + dirName,
            fileName
        )
        res.putString("uriString", Uri.fromFile(file).toString())
        res.putString("filePath", file.absolutePath)
        res.putString("fileName", fileName)
        res.putString("fileType", fileType)
        res.putBoolean("isImage", isImage)
        res.putSerializable("extFile", this)
        return res
    }
}