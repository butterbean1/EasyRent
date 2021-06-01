package ru.butterbean.easyrent.screens.reserves

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.reserve_item.view.*
import ru.butterbean.easyrent.CURRENT_RESERVE
import ru.butterbean.easyrent.R
import ru.butterbean.easyrent.models.ReserveData
import ru.butterbean.easyrent.utils.replaceFragment
import ru.butterbean.easyrent.utils.toDateTimeFormat

class ReservesListAdapter:RecyclerView.Adapter<ReservesListAdapter.ReservesListHolder>() {

    private var listReserves = emptyList<ReserveData>()

    class ReservesListHolder(view: View):RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservesListHolder {
        val holder = ReservesListHolder(LayoutInflater.from(parent.context).inflate(R.layout.reserve_item,parent,false) )
        holder.itemView.setOnClickListener {
            CURRENT_RESERVE = listReserves[holder.adapterPosition]
            replaceFragment(EditReserveFragment())
        }
        return holder
    }

    override fun onBindViewHolder(holder: ReservesListHolder, position: Int) {
        val currentItem = listReserves[position]
        holder.itemView.reserves_list_date_check_in.text = currentItem.dateCheckIn.toDateTimeFormat(false,false)
        holder.itemView.reserves_list_date_check_out.text = currentItem.dateCheckOut.toDateTimeFormat(false,false)
        holder.itemView.reserves_list_time_check_in.text = currentItem.dateCheckIn.toDateTimeFormat(true,false)
        holder.itemView.reserves_list_time_check_out.text = currentItem.dateCheckOut.toDateTimeFormat(true,false)
    }

    override fun getItemCount(): Int = listReserves.size

    fun setData(reserves:List<ReserveData>){
        listReserves = reserves
        notifyDataSetChanged()

    }
}