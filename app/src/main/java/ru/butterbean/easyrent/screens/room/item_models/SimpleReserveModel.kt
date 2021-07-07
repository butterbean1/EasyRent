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
    val wasCheckOut: Boolean = false,
    val phoneNumber: String = "",
    val extFilesCount: Int = 0,
) : CommonReserveModel {

    override fun getItemViewType(): Int = CommonReserveModel.SIMPLE

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder) {
        val holder = viewHolder as ReservesViewHolderFactory.SimpleReserveHolder
        holder.guestName.text = guestName
        holder.guestsCount.text = guestsCount.toString()
        if (sum == 0) holder.sumsGroup.visibility = View.GONE
        else {
            holder.sumsGroup.visibility = View.VISIBLE
            holder.sum.text = sum.toString()
            holder.sumCheck.visibility = if (sum in 1..payment) View.VISIBLE else View.GONE
        }
        holder.dateCheckIn.text = dateCheckIn.toDateTimeFormat()
        holder.dateCheckOut.text = dateCheckOut.toDateTimeFormat()
        holder.wasCheckIn.visibility = if (wasCheckIn) View.VISIBLE else View.GONE
        holder.wasCheckOut.visibility = if (wasCheckOut) View.VISIBLE else View.GONE
        if (reserveCompleted(wasCheckOut, sum, payment)) {
            holder.itemView.background =
                AppCompatResources.getDrawable(APP_ACTIVITY, R.drawable.ripple_effect_green)
        } else {
            val today = getStartOfDay(Calendar.getInstance())
            if ((today.after(getStartOfDay(getCalendarFromString(dateCheckIn))) && !wasCheckIn)
                || (today.after(getStartOfDay(getCalendarFromString(dateCheckOut))) && !wasCheckOut)
            ) {
                holder.itemView.background =
                    AppCompatResources.getDrawable(APP_ACTIVITY, R.drawable.ripple_effect_pink)
            } else {
                holder.itemView.background =
                    AppCompatResources.getDrawable(APP_ACTIVITY, R.drawable.ripple_effect)
            }
        }
        if (extFilesCount == 0) holder.attachmentGroup.visibility = View.GONE
        else {
            holder.attachmentGroup.visibility = View.VISIBLE
            holder.attachmentText.text = extFilesCount.toString()
        }
    }

    override fun toReserveData(): ReserveData {
        return ReserveData(
            id,
            roomId,
            guestName,
            guestsCount,
            sum,
            payment,
            dateCheckIn,
            dateCheckOut,
            wasCheckIn,
            wasCheckOut,
            phoneNumber,
            extFilesCount
        )
    }

}