package ru.butterbean.easyrent.screens.reserves_archive

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import ru.butterbean.easyrent.R
import ru.butterbean.easyrent.databinding.ReserveItemBinding
import ru.butterbean.easyrent.models.ReserveArchiveData
import ru.butterbean.easyrent.utils.APP_ACTIVITY
import ru.butterbean.easyrent.utils.toDateTimeFormat

class ArchiveReservesListAdapter(private val f: ArchiveReservesFragment) :
    RecyclerView.Adapter<ArchiveReservesListAdapter.ArchiveReservesListHolder>() {

    private var mList = emptyList<ReserveArchiveData>()

    class ArchiveReservesListHolder(itemBinding: ReserveItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        val guestName: TextView = itemBinding.reservesListGuestName
        val guestsCount: TextView = itemBinding.reservesListGuestsCount
        val sum: TextView = itemBinding.reservesListSum
        val sumCheck: ImageView = itemBinding.reservesListSumCheck
        val dateCheckIn: TextView = itemBinding.reservesListDateCheckIn
        val dateCheckOut: TextView = itemBinding.reservesListDateCheckOut
        val wasCheckIn: ImageView = itemBinding.reservesListWasCheckIn
        val wasCheckOut: ImageView = itemBinding.reservesListWasCheckOut
        val markItem: ImageView = itemBinding.reservesListMarkItem
        val nonMarkItem: ImageView = itemBinding.reservesListNonMarkItem
    }

    override fun onViewAttachedToWindow(holder: ArchiveReservesListHolder) {
        holder.itemView.setOnClickListener {
            if (f.listMarkedReserves.count() == 0) {
                f.goToEditReserveFragment(mList[holder.adapterPosition])
            } else changeMarkItem(mList[holder.adapterPosition], holder)
        }
        holder.itemView.setOnLongClickListener {
            changeMarkItem(mList[holder.adapterPosition], holder)
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
        holder.sumCheck.visibility =
            if (currentItem.sum > 0 && currentItem.sum <= currentItem.payment) View.VISIBLE else View.INVISIBLE
        holder.dateCheckIn.text = currentItem.dateCheckIn.toDateTimeFormat()
        holder.dateCheckOut.text = currentItem.dateCheckOut.toDateTimeFormat()
        holder.wasCheckIn.visibility = if (currentItem.wasCheckIn) View.VISIBLE else View.GONE
        holder.itemView.background =
            AppCompatResources.getDrawable(APP_ACTIVITY, R.drawable.ripple_effect_grey)
        if (f.listMarkedReserves.contains(currentItem)) holder.markItem.visibility = View.VISIBLE
        else holder.markItem.visibility = View.GONE
    }

    private fun changeMarkItem(
        reserve: ReserveArchiveData,
        holder: ArchiveReservesListHolder
    ) {

        if (f.listMarkedReserves.contains(reserve)) {
            f.listMarkedReserves.remove(reserve)
            holder.markItem.visibility = View.GONE
        } else {
            f.listMarkedReserves.add(reserve)
            holder.markItem.visibility = View.VISIBLE
        }
        f.setVisibleOptionsMenuItems()

    }

    override fun getItemCount(): Int = mList.size

    fun setData(l: List<ReserveArchiveData>) {
        mList = l
        notifyDataSetChanged()
    }
}
