package ru.butterbean.easyrent.screens.room

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.room_item.view.*
import ru.butterbean.easyrent.R
import ru.butterbean.easyrent.models.RoomData

class RoomsListAdapter(private val lo: LifecycleOwner) :RecyclerView.Adapter<RoomsListAdapter.RoomsListHolder>() {

    private var listRooms = emptyList<RoomData>()

    class RoomsListHolder(view: View):RecyclerView.ViewHolder(view)

    override fun onViewAttachedToWindow(holder: RoomsListHolder) {
        val currentRoom = listRooms[holder.adapterPosition]
        holder.itemView.setOnClickListener {
            RoomsListFragment.clickOnListItem(currentRoom)
             }
        holder.itemView.setOnLongClickListener {
            RoomsListFragment.longClickOnListItem(currentRoom,lo)
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
            LayoutInflater.from(parent.context).inflate(R.layout.room_item, parent, false)
        )
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