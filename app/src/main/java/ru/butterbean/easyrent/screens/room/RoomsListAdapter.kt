package ru.butterbean.easyrent.screens.room

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.room_item.view.*
import ru.butterbean.easyrent.R
import ru.butterbean.easyrent.database.view_models.RoomViewModel
import ru.butterbean.easyrent.database.models.RoomData
import ru.butterbean.easyrent.utils.APP_ACTIVITY
import ru.butterbean.easyrent.utils.replaceFragment
import ru.butterbean.easyrent.utils.showEditDeleteRoomDialog

class RoomsListAdapter(private val f: Fragment) :RecyclerView.Adapter<RoomsListAdapter.RoomsListHolder>() {

    private var listRooms = emptyList<RoomData>()

    class RoomsListHolder(view: View):RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomsListHolder {
        val holder = RoomsListHolder(LayoutInflater.from(parent.context).inflate(R.layout.room_item,parent,false) )
        val roomViewModel = ViewModelProvider(APP_ACTIVITY).get(RoomViewModel::class.java)
        holder.itemView.setOnClickListener {
            roomViewModel.currentRoom = listRooms[holder.adapterPosition]
            replaceFragment(RoomFragment())
        }
        holder.itemView.setOnLongClickListener {
            roomViewModel.currentRoom = listRooms[holder.adapterPosition]
            showEditDeleteRoomDialog(roomViewModel.currentRoom,f.viewLifecycleOwner)
            true
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