package ru.butterbean.easyrent.screens.room

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.room_item.view.*
import ru.butterbean.easyrent.CURRENT_ROOM
import ru.butterbean.easyrent.R
import ru.butterbean.easyrent.models.RoomData
import ru.butterbean.easyrent.utils.replaceFragment

class RoomsListAdapter:RecyclerView.Adapter<RoomsListAdapter.RoomsListHolder>() {

    private var listRooms = emptyList<RoomData>()

    class RoomsListHolder(view: View):RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomsListHolder {
        val holder = RoomsListHolder(LayoutInflater.from(parent.context).inflate(R.layout.room_item,parent,false) )
        holder.itemView.setOnClickListener {
            CURRENT_ROOM = listRooms[holder.adapterPosition]
            replaceFragment(RoomFragment())
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