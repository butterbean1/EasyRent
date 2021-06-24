package ru.butterbean.easyrent.screens.room.item_models

import androidx.recyclerview.widget.RecyclerView
import ru.butterbean.easyrent.R
import ru.butterbean.easyrent.models.ReserveData
import ru.butterbean.easyrent.screens.room.ReservesViewHolderFactory
import ru.butterbean.easyrent.utils.APP_ACTIVITY

class ArchiveReserveModel(
    val roomId: Long,
    private val archivedCount: Int
) : CommonReserveModel {
    override fun getItemViewType(): Int = CommonReserveModel.ARCHIVE

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder){
        val holder = viewHolder as ReservesViewHolderFactory.ArchiveReserveHolder
        val stringReserves = archivedCount.toString()
        val t = "$stringReserves ${APP_ACTIVITY.getString(R.string.in_the_archive)}"
        holder.text.text = t
    }

    override fun toReserveData(): ReserveData? {
        return null
    }
}