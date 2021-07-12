package ru.butterbean.easyrent.screens.costs_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.butterbean.easyrent.databinding.CostsListItemBinding
import ru.butterbean.easyrent.databinding.ExtFileItemBinding
import ru.butterbean.easyrent.models.CostData
import ru.butterbean.easyrent.models.ReserveExtFileData
import ru.butterbean.easyrent.utils.getDateFormatISO
import ru.butterbean.easyrent.utils.setExtFileImage
import ru.butterbean.easyrent.utils.toDateFormat
import ru.butterbean.easyrent.utils.trimFileName

class CostsListAdapter(private val f:CostsListFragment) : RecyclerView.Adapter<CostsListAdapter.CostsListHolder>() {

    private var listCosts = emptyList<CostsListItem>()

    class CostsListHolder(itemBinding: CostsListItemBinding): RecyclerView.ViewHolder(itemBinding.root){
        val costName = itemBinding.costsListCostName
        val date = itemBinding.costsListDate
        val sum = itemBinding.costsListSum
    }

    override fun onViewAttachedToWindow(holder: CostsListHolder) {
        holder.itemView.setOnClickListener {
            CostsListFragment.clickOnListItem(listCosts[holder.adapterPosition].id,f)
        }
        holder.itemView.setOnLongClickListener {
            CostsListFragment.longClickOnListItem(listCosts[holder.adapterPosition].id,f)
            true
        }
        super.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: CostsListHolder) {
        holder.itemView.setOnClickListener(null)
        holder.itemView.setOnLongClickListener(null)
        super.onViewDetachedFromWindow(holder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CostsListHolder {

        return CostsListHolder(
            CostsListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: CostsListHolder, position: Int) {
        val currentItem = listCosts[position]
        holder.date.text = currentItem.date.toDateFormat()
        holder.sum.text = currentItem.sum.toString()
        holder.costName.text = currentItem.itemName
    }

    override fun getItemCount(): Int = listCosts.size

    fun setData(costs:List<CostsListItem>){
        listCosts = costs
        notifyDataSetChanged()

    }
}