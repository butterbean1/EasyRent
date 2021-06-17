package ru.butterbean.easyrent.screens.reserves_archive

import androidx.recyclerview.widget.RecyclerView
import ru.butterbean.easyrent.R
import ru.butterbean.easyrent.models.ReserveData
import ru.butterbean.easyrent.screens.reserves.ReserveType
import ru.butterbean.easyrent.screens.reserves.ReservesViewHolderFactory
import ru.butterbean.easyrent.utils.APP_ACTIVITY

class ArchiveReserveModel(
    val roomId: Long,
    private val archivedCount: Int
) : ReserveType {
    override fun getItemViewType(): Int = ReserveType.ARCHIVE

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder){
        val holder = viewHolder as ReservesViewHolderFactory.ArchiveReserveHolder
        val stringReserves = APP_ACTIVITY.resources.getQuantityString(R.plurals.reserves_count,archivedCount,archivedCount)
        val t = "$stringReserves ${APP_ACTIVITY.getString(R.string.in_the_archive)}"
        holder.text.text = t
    }

    override fun toReserveData(): ReserveData? {
        return null
    }
}