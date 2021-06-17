package ru.butterbean.easyrent.screens.room

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import ru.butterbean.easyrent.screens.reserves_archive.ArchiveReservesFragment
import ru.butterbean.easyrent.R
import ru.butterbean.easyrent.databinding.ReserveItemBinding
import ru.butterbean.easyrent.models.ReserveArchiveData
import ru.butterbean.easyrent.utils.APP_ACTIVITY
import ru.butterbean.easyrent.utils.getCalendarFromString
import ru.butterbean.easyrent.utils.getStartOfDay
import ru.butterbean.easyrent.utils.toDateTimeFormat
import java.util.*

class ArchiveReservesListAdapter() :RecyclerView.Adapter<ArchiveReservesListAdapter.ArchiveReservesListHolder>() {

    private var mList = emptyList<ReserveArchiveData>()

    class ArchiveReservesListHolder(itemBinding: ReserveItemBinding):RecyclerView.ViewHolder(itemBinding.root){
        val guestName: TextView = itemBinding.reservesListGuestName
        val guestsCount: TextView = itemBinding.reservesListGuestsCount
        val sum: TextView = itemBinding.reservesListSum
        val sumCheck: ImageView = itemBinding.reservesListSumCheck
        val dateCheckIn: TextView = itemBinding.reservesListDateCheckIn
        val dateCheckOut: TextView = itemBinding.reservesListDateCheckOut
        val wasCheckIn: ImageView = itemBinding.reservesListWasCheckIn
        val wasCheckOut: ImageView = itemBinding.reservesListWasCheckOut
    }

    override fun onViewAttachedToWindow(holder: ArchiveReservesListHolder) {
        holder.itemView.setOnClickListener {
            ArchiveReservesFragment.clickOnListItem(mList[holder.adapterPosition])
             }
        holder.itemView.setOnLongClickListener {
            ArchiveReservesFragment.longClickOnListItem(mList[holder.adapterPosition])
            true
        }
        super.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: ArchiveReservesListHolder) {
        holder.itemView.setOnClickListener(null)
        holder.itemView.setOnLongClickListener(null)
        super.onViewDetachedFromWindow(holder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArchiveReservesListHolder {

        return ArchiveReservesListHolder(
            ReserveItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ArchiveReservesListHolder, position: Int) {
        val currentItem = mList[position]
        holder.guestName.text = currentItem.guestName
        holder.guestsCount.text = currentItem.guestsCount.toString()
        holder.sum.text = currentItem.sum.toString()
        holder.sumCheck.visibility = if (currentItem.sum > 0 && currentItem.sum <= currentItem.payment) View.VISIBLE else View.INVISIBLE
        holder.dateCheckIn.text = currentItem.dateCheckIn.toDateTimeFormat()
        holder.dateCheckOut.text = currentItem.dateCheckOut.toDateTimeFormat()
        holder.wasCheckIn.visibility = if (currentItem.wasCheckIn) View.VISIBLE else View.GONE
        if (currentItem.wasCheckOut) {
            holder.wasCheckOut.visibility = View.VISIBLE
            holder.itemView.background =
                AppCompatResources.getDrawable(APP_ACTIVITY, R.drawable.ripple_effect_grey)
        } else {
            holder.wasCheckOut.visibility = View.GONE
            val today = getStartOfDay(Calendar.getInstance())
            if ((today.after(getStartOfDay(getCalendarFromString(currentItem.dateCheckIn))) && !currentItem.wasCheckIn)
                || (today.after(getStartOfDay(getCalendarFromString(currentItem.dateCheckOut))) && !currentItem.wasCheckOut)){
                holder.itemView.background =
                    AppCompatResources.getDrawable(APP_ACTIVITY, R.drawable.ripple_effect_pink)
            }
        }
    }

    override fun getItemCount(): Int = mList.size

    fun setData(l:List<ReserveArchiveData>){
        mList = l
        notifyDataSetChanged()

    }
}