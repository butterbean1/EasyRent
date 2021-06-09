package ru.butterbean.easyrent.screens.reserves

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.butterbean.easyrent.screens.room.RoomFragment

class ReservesListAdapterNew(val fragment: RoomFragment) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var listReserves = emptyList<ReserveType>()

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        holder.itemView.setOnClickListener {
            RoomFragment.clickOnListItem(listReserves[holder.adapterPosition])
        }
        holder.itemView.setOnLongClickListener {
            RoomFragment.longClickOnListItem(listReserves[holder.adapterPosition],fragment)
            true
        }
        super.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        holder.itemView.setOnClickListener(null)
        holder.itemView.setOnLongClickListener(null)
        super.onViewDetachedFromWindow(holder)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return ReservesViewHolderFactory.create(parent,viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        listReserves[position].onBindViewHolder(holder)
    }

    override fun getItemCount(): Int = listReserves.size

    fun setData(reserves:List<ReserveType>){
        listReserves = reserves
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return listReserves[position].getItemViewType()
    }
}