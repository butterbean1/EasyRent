package ru.butterbean.easyrent.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.butterbean.easyrent.database.TABLE_COST_ITEMS_NAME
import java.io.Serializable

@Entity(tableName = TABLE_COST_ITEMS_NAME)
data class CostItemData(
    @PrimaryKey(autoGenerate = true)
    val id:Long,
    val name:String = ""
):Serializable
