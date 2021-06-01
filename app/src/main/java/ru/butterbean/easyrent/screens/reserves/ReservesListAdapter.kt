package ru.butterbean.easyrent.screens.reserves

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.reserve_item.view.*
import ru.butterbean.easyrent.database.CURRENT_RESERVE
import ru.butterbean.easyrent.R
import ru.butterbean.easyrent.models.ReserveData
import ru.butterbean.easyrent.utils.*

class ReservesListAdapter:RecyclerView.Adapter<ReservesListAdapter.ReservesListHolder>() {

    private var listReserves = emptyList<ReserveData>()

    class ReservesListHolder(view: View):RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservesListHolder {
        val holder = ReservesListHolder(LayoutInflater.from(parent.context).inflate(R.layout.reserve_item,parent,false) )
        holder.itemView.setOnClickListener {
            CURRENT_RESERVE = listReserves[holder.adapterPosition]
            replaceFragment(EditReserveFragment())
        }
        holder.itemView.setOnLongClickListener {
            CURRENT_RESERVE = listReserves[holder.adapterPosition]
            showEditDeleteReserveDialog(CURRENT_RESERVE)
            true
        }
        return holder
    }

    override fun onBindViewHolder(holder: ReservesListHolder, position: Int) {
        val currentItem = listReserves[position]
        holder.itemView.reserves_list_date_check_in.text = currentItem.dateCheckIn.toDateTimeFormat()
        holder.itemView.reserves_list_date_check_out.text = currentItem.dateCheckOut.toDateTimeFormat()
        holder.itemView.reserves_list_was_check_in.visibility = if(currentItem.wasCheckIn) View.VISIBLE else View.GONE
        holder.itemView.reserves_list_was_check_out.visibility = if(currentItem.wasCheckOut) View.VISIBLE else View.GONE
    }

    override fun getItemCount(): Int = listReserves.size

    fun setData(reserves:List<ReserveData>){
        listReserves = reserves
        notifyDataSetChanged()

    }
}