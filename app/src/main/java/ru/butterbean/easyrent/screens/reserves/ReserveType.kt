package ru.butterbean.easyrent.screens.reserves

import androidx.recyclerview.widget.RecyclerView
import ru.butterbean.easyrent.models.ReserveData

interface ReserveType {
    companion object{
        const val SIMPLE = 0
        const val FREE = 1
        const val ARCHIVE = 2
    }

    fun toReserveData(): ReserveData?

    fun getItemViewType():Int

    fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder)
}