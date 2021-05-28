package ru.butterbean.easyrent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.room_item.view.*
import ru.butterbean.easyrent.models.RoomData
import ru.butterbean.easyrent.screens.RoomsListFragmentDirections

class RoomsListAdapter:RecyclerView.Adapter<RoomsListAdapter.RoomsListHolder>() {

    private var listRooms = emptyList<RoomData>()

    class RoomsListHolder(view: View):RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomsListHolder {
        val holder = RoomsListHolder(LayoutInflater.from(parent.context).inflate(R.layout.room_item,parent,false) )
        holder.itemView.setOnClickListener {
            val action = RoomsListFragmentDirections.actionRoomsListFragmentToRoomChangeFragment(listRooms[holder.adapterPosition])
            holder.itemView.findNavController().navigate(action)
        }
        return holder
    }

    override fun onBindViewHolder(holder: RoomsListHolder, position: Int) {
        val currentItem = listRooms[position]
        holder.itemView.rooms_list_room_name.text = currentItem.name
        holder.itemView.rooms_list_room_status.text = currentItem.status
    }

    override fun getItemCount(): Int = listRooms.size

    fun setData(rooms:List<RoomData>){
        listRooms = rooms
        notifyDataSetChanged()

    }
}