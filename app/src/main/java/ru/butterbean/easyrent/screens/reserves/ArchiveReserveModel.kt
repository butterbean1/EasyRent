package ru.butterbean.easyrent.screens.reserves

import androidx.recyclerview.widget.RecyclerView
import ru.butterbean.easyrent.models.ReserveData
import ru.butterbean.easyrent.utils.*

class ArchiveReserveModel(
    val roomId: Long
) : ReserveType {
    override fun getItemViewType(): Int = ReserveType.ARCHIVE

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder){}

    override fun toReserveData(): ReserveData? {
        return null
    }
}