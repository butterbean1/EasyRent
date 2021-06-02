package ru.butterbean.easyrent.screens.reserves

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.reserve_item.view.*
import ru.butterbean.easyrent.R
import ru.butterbean.easyrent.database.view_models.ReserveViewModel
import ru.butterbean.easyrent.models.ReserveData
import ru.butterbean.easyrent.utils.APP_ACTIVITY
import ru.butterbean.easyrent.utils.replaceFragment
import ru.butterbean.easyrent.utils.showEditDeleteReserveDialog
import ru.butterbean.easyrent.utils.toDateTimeFormat

class ReservesListAdapter :RecyclerView.Adapter<ReservesListAdapter.ReservesListHolder>() {

    private var listReserves = emptyList<ReserveData>()

    class ReservesListHolder(view: View):RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservesListHolder {
        val holder = ReservesListHolder(LayoutInflater.from(parent.context).inflate(R.layout.reserve_item,parent,false) )
        val reserveViewModel = ViewModelProvider(APP_ACTIVITY).get(ReserveViewModel::class.java)
        holder.itemView.setOnClickListener {
            reserveViewModel.currentReserve = listReserves[holder.adapterPosition]
            replaceFragment(EditReserveFragment())
        }
        holder.itemView.setOnLongClickListener {
            reserveViewModel.currentReserve = listReserves[holder.adapterPosition]
            showEditDeleteReserveDialog(reserveViewModel.currentReserve)
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