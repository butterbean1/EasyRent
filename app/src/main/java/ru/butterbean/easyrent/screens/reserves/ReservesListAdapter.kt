package ru.butterbean.easyrent.screens.reserves
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.appcompat.content.res.AppCompatResources
//import androidx.recyclerview.widget.RecyclerView
//import kotlinx.android.synthetic.main.reserve_item.view.*
//import ru.butterbean.easyrent.R
//import ru.butterbean.easyrent.models.ReserveData
//import ru.butterbean.easyrent.screens.room.RoomFragment
//import ru.butterbean.easyrent.utils.APP_ACTIVITY
//import ru.butterbean.easyrent.utils.toDateTimeFormat
//
//class ReservesListAdapter(val fragment: RoomFragment) :RecyclerView.Adapter<ReservesListAdapter.ReservesListHolder>() {
//
//    private var listReserves = emptyList<ReserveData>()
//
//    class ReservesListHolder(view: View):RecyclerView.ViewHolder(view)
//
//    override fun onViewAttachedToWindow(holder: ReservesListHolder) {
//        holder.itemView.setOnClickListener {
//            RoomFragment.clickOnListItem(listReserves[holder.adapterPosition])
//        }
//        holder.itemView.setOnLongClickListener {
//            RoomFragment.longClickOnListItem(listReserves[holder.adapterPosition],fragment)
//            true
//        }
//        super.onViewAttachedToWindow(holder)
//    }
//
//    override fun onViewDetachedFromWindow(holder: ReservesListHolder) {
//        holder.itemView.setOnClickListener(null)
//        holder.itemView.setOnLongClickListener(null)
//        super.onViewDetachedFromWindow(holder)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservesListHolder {
//        return ReservesListHolder(LayoutInflater.from(parent.context).inflate(R.layout.reserve_item, parent, false))
//    }
//
//    override fun onBindViewHolder(holder: ReservesListHolder, position: Int) {
//        val currentItem = listReserves[position]
//        holder.itemView.reserves_list_guest_name.text = currentItem.guestName
//        holder.itemView.reserves_list_guests_count.text = currentItem.guestsCount.toString()
//        holder.itemView.reserves_list_sum.text = currentItem.sum.toString()
//        holder.itemView.reserves_list_sum_check.visibility = if(currentItem.sum>0 && currentItem.sum<=currentItem.payment) View.VISIBLE else View.INVISIBLE
//        holder.itemView.reserves_list_date_check_in.text = currentItem.dateCheckIn.toDateTimeFormat()
//        holder.itemView.reserves_list_date_check_out.text = currentItem.dateCheckOut.toDateTimeFormat()
//        holder.itemView.reserves_list_was_check_in.visibility = if(currentItem.wasCheckIn) View.VISIBLE else View.GONE
//        if (currentItem.wasCheckOut){
//            holder.itemView.reserves_list_was_check_out.visibility = View.VISIBLE
//            holder.itemView.background =  AppCompatResources.getDrawable(APP_ACTIVITY,R.drawable.ripple_effect_grey)
//        }else holder.itemView.reserves_list_was_check_out.visibility = View.GONE
//
//    }
//
//    override fun getItemCount(): Int = listReserves.size
//
//    fun setData(reserves:List<ReserveData>){
//        listReserves = reserves
//        notifyDataSetChanged()
//    }
//}