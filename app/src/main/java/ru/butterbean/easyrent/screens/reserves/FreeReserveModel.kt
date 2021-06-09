package ru.butterbean.easyrent.screens.reserves

import androidx.recyclerview.widget.RecyclerView
import ru.butterbean.easyrent.models.ReserveData
import ru.butterbean.easyrent.utils.*

class FreeReserveModel(
    val roomId: Long,
    val dateCheckIn: String,
    val dateCheckOut: String = ""
) : ReserveType {
    override fun getItemViewType(): Int = ReserveType.FREE

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder) {
        val holder = viewHolder as ReservesViewHolderFactory.FreeReserveHolder
        val t = STATUS_FREE +
                when {
                    // дата начала интервала не выбрана
                    dateCheckIn.isEmpty() -> " $STATUS_UNTIL ${dateCheckOut.toDateTimeFormat()}"
                    // дата завершения интервала не выбрана
                    dateCheckOut.isEmpty() -> " $STATUS_FREE_FROM ${dateCheckIn.toDateTimeFormat()}"
                    // даты начала и завершения равны
                    (getStartOfDay(getCalendarFromString(dateCheckIn)) == getStartOfDay(
                        getCalendarFromString(dateCheckOut)
                    )) -> "$STATUS_FREE_ON ${dateCheckIn.toDateFormat()}"
                    // обычные интервалы
                    else -> " $STATUS_FREE_FROM ${dateCheckIn.toDateFormat()} $STATUS_FREE_TO ${dateCheckOut.toDateFormat()}"
                }
        holder.text.text = t
    }

    override fun toReserveData(): ReserveData {
        return ReserveData(
            0,
            this.roomId,
            "",
            0,
            0,
            0,
            this.dateCheckIn,
            this.dateCheckOut,
            false,
            false
        )
    }
}