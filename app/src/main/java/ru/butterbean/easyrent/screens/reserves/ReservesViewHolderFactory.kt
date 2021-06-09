package ru.butterbean.easyrent.screens.reserves

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.free_reserve_item.view.*
import kotlinx.android.synthetic.main.reserve_item.view.*
import ru.butterbean.easyrent.R

class ReservesViewHolderFactory {
    class SimpleReserveHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val guestName: TextView = itemView.reserves_list_guest_name
        val guestsCount: TextView = itemView.reserves_list_guests_count
        val sum: TextView = itemView.reserves_list_sum
        val sumCheck: ImageView = itemView.reserves_list_sum_check
        val dateCheckIn: TextView = itemView.reserves_list_date_check_in
        val dateCheckOut: TextView = itemView.reserves_list_date_check_out
        val wasCheckIn: ImageView = itemView.reserves_list_was_check_in
        val wasCheckOut: ImageView = itemView.reserves_list_was_check_out
    }

    class FreeReserveHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val text: TextView = itemView.reserves_list_free_reserve
    }

    companion object {
        fun create(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when (viewType) {
                ReserveType.SIMPLE -> SimpleReserveHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.reserve_item, parent, false)
                )
                else -> FreeReserveHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.free_reserve_item, parent, false)
                )
            }
        }
    }
}