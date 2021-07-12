package ru.butterbean.easyrent.screens.cost_items_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.butterbean.easyrent.databinding.CostItemsListItemBinding
import ru.butterbean.easyrent.databinding.ExtFileItemBinding
import ru.butterbean.easyrent.models.CostData
import ru.butterbean.easyrent.models.CostItemData
import ru.butterbean.easyrent.models.ReserveExtFileData
import ru.butterbean.easyrent.utils.setExtFileImage
import ru.butterbean.easyrent.utils.toDateFormat
import ru.butterbean.easyrent.utils.trimFileName

class CostItemsListAdapter(private val f:CostItemsListFragment) : RecyclerView.Adapter<CostItemsListAdapter.CostsListHolder>() {

    private var listCosts = emptyList<CostItemData>()

    class CostsListHolder(itemBinding: CostItemsListItemBinding): RecyclerView.ViewHolder(itemBinding.root){
        val name = itemBinding.costItemsListName
    }

    override fun onViewAttachedToWindow(holder: CostsListHolder) {
        holder.itemView.setOnClickListener {
            CostItemsListFragment.clickOnListItem(listCosts[holder.adapterPosition].id,f)
        }
        holder.itemView.setOnLongClickListener {
            CostItemsListFragment.longClickOnListItem(listCosts[holder.adapterPosition].id,f)
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
            CostItemsListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: CostsListHolder, position: Int) {
        val currentItem = listCosts[position]
        holder.name.text = currentItem.name
    }

    override fun getItemCount(): Int = listCosts.size

    fun setData(costItems:List<CostItemData>){
        listCosts = costItems
        notifyDataSetChanged()

    }
}