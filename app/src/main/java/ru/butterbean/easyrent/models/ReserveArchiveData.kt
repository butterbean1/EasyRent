package ru.butterbean.easyrent.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import ru.butterbean.easyrent.database.TABLE_RESERVES_ARCHIVE_NAME
import java.io.Serializable

@Entity(
    tableName = TABLE_RESERVES_ARCHIVE_NAME,
    foreignKeys = [ForeignKey(
        entity = RoomData::class,
        parentColumns = ["id"],
        childColumns = ["roomId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class ReserveArchiveData(
    @PrimaryKey(autoGenerate = true)
    override val id: Long,
    @ColumnInfo(index = true)
    override val roomId: Long,
    override val guestName: String = "",
    override val guestsCount: Int = 0,
    override val sum: Int = 0,
    override val payment: Int = 0,
    override val dateCheckIn: String = "",
    override val dateCheckOut: String = "",
    override val wasCheckIn: Boolean = false,
    override val wasCheckOut: Boolean = false
) : CommonReserveData(
    id,
    roomId,
    guestName,
    guestsCount,
    sum,
    payment,
    dateCheckIn,
    dateCheckOut,
    wasCheckIn,
    wasCheckOut
), Serializable {
    override fun isComplete(): Boolean {
        return true
    }
}
