package ru.butterbean.easyrent.screens.room

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.butterbean.easyrent.databinding.FreeReserveItemBinding
import ru.butterbean.easyrent.databinding.LinkArchiveItemBinding
import ru.butterbean.easyrent.databinding.ReserveItemBinding
import ru.butterbean.easyrent.screens.room.item_models.CommonReserveModel

class ReservesViewHolderFactory {
    class SimpleReserveHolder(itemBinding: ReserveItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        val guestName: TextView = itemBinding.reservesListGuestName
        val guestsCount: TextView = itemBinding.reservesListGuestsCount
        val sum: TextView = itemBinding.reservesListSum
        val sumCheck: ImageView = itemBinding.reservesListSumCheck
        val dateCheckIn: TextView = itemBinding.reservesListDateCheckIn
        val dateCheckOut: TextView = itemBinding.reservesListDateCheckOut
        val wasCheckIn: ImageView = itemBinding.reservesListWasCheckIn
        val wasCheckOut: ImageView = itemBinding.reservesListWasCheckOut
    }

    class FreeReserveHolder(itemBinding: FreeReserveItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        val text: TextView = itemBinding.reservesListFreeReserve
    }

    class ArchiveReserveHolder(itemBinding: LinkArchiveItemBinding) : RecyclerView.ViewHolder(itemBinding.root){
        val text:TextView = itemBinding.reservesListLinkToArchive
    }

    companion object {
        fun create(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when (viewType) {
                CommonReserveModel.SIMPLE -> SimpleReserveHolder(
                    ReserveItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
                CommonReserveModel.ARCHIVE -> ArchiveReserveHolder(
                    LinkArchiveItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                )
                else -> FreeReserveHolder(
                    FreeReserveItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }
}