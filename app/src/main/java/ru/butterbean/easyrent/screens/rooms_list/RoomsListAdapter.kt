package ru.butterbean.easyrent.screens.rooms_list

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import ru.butterbean.easyrent.databinding.RoomItemBinding
import ru.butterbean.easyrent.models.RoomData

class RoomsListAdapter(private val lo: LifecycleOwner) :RecyclerView.Adapter<RoomsListAdapter.RoomsListHolder>() {

    private var listRooms = emptyList<RoomData>()

    class RoomsListHolder(itemBinding: RoomItemBinding):RecyclerView.ViewHolder(itemBinding.root){
        val roomName:TextView = itemBinding.roomsListRoomName
        val roomStatus:TextView = itemBinding.roomsListRoomStatus
    }

    override fun onViewAttachedToWindow(holder: RoomsListHolder) {
        holder.itemView.setOnClickListener {
            RoomsListFragment.clickOnListItem(listRooms[holder.adapterPosition])
             }
        holder.itemView.setOnLongClickListener {
            RoomsListFragment.longClickOnListItem(listRooms[holder.adapterPosition], lo)
            true
        }
        super.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: RoomsListHolder) {
        holder.itemView.setOnClickListener(null)
        holder.itemView.setOnLongClickListener(null)
        super.onViewDetachedFromWindow(holder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomsListHolder {

        return RoomsListHolder(
            RoomItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: RoomsListHolder, position: Int) {
        val currentItem = listRooms[position]
        holder.roomName.text = currentItem.name
        holder.roomStatus.text = currentItem.status
    }

    override fun getItemCount(): Int = listRooms.size

    fun setData(rooms:List<RoomData>){
        listRooms = rooms
        notifyDataSetChanged()

    }
}