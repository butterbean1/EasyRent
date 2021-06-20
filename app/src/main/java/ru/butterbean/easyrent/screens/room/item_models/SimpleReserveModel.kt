package ru.butterbean.easyrent.screens.room.item_models

import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import ru.butterbean.easyrent.R
import ru.butterbean.easyrent.models.ReserveData
import ru.butterbean.easyrent.screens.room.ReservesViewHolderFactory
import ru.butterbean.easyrent.utils.*
import java.util.*

class SimpleReserveModel(
    val id: Long,
    val roomId: Long,
    val guestName: String = "",
    val guestsCount: Int = 0,
    val sum: Int = 0,
    val payment: Int = 0,
    val dateCheckIn: String = "",
    val dateCheckOut: String = "",
    val wasCheckIn: Boolean = false,
    val wasCheckOut: Boolean = false
) : CommonReserveModel {

    override fun getItemViewType(): Int = CommonReserveModel.SIMPLE

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder) {
        val holder = viewHolder as ReservesViewHolderFactory.SimpleReserveHolder
        holder.guestName.text = guestName
        holder.guestsCount.text = guestsCount.toString()
        holder.sum.text = sum.toString()
        holder.sumCheck.visibility = if (sum in 1..payment) View.VISIBLE else View.INVISIBLE
        holder.dateCheckIn.text = dateCheckIn.toDateTimeFormat()
        holder.dateCheckOut.text = dateCheckOut.toDateTimeFormat()
        holder.wasCheckIn.visibility = if (wasCheckIn) View.VISIBLE else View.GONE
        holder.wasCheckOut.visibility = if (wasCheckOut) View.VISIBLE else View.GONE
        if (reserveCompleted(wasCheckOut, sum, payment)) {
            holder.itemView.background =
                AppCompatResources.getDrawable(APP_ACTIVITY, R.drawable.ripple_effect_grey)
        } else {
            val today = getStartOfDay(Calendar.getInstance())
            if ((today.after(getStartOfDay(getCalendarFromString(dateCheckIn))) && !wasCheckIn)
                || (today.after(getStartOfDay(getCalendarFromString(dateCheckOut))) && !wasCheckOut)
            ) {
                holder.itemView.background =
                    AppCompatResources.getDrawable(APP_ACTIVITY, R.drawable.ripple_effect_pink)
            }
        }
    }

    override fun toReserveData(): ReserveData {
        return ReserveData(
            this.id,
            this.roomId,
            this.guestName,
            this.guestsCount,
            this.sum,
            this.payment,
            this.dateCheckIn,
            this.dateCheckOut,
            this.wasCheckIn,
            this.wasCheckOut,
        )
    }

}