package ru.butterbean.easyrent.screens.ext_files

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import ru.butterbean.easyrent.databinding.ExtFileItemBinding
import ru.butterbean.easyrent.models.ReserveExtFileData
import ru.butterbean.easyrent.utils.setExtFileImage

class ExtFilesListAdapter(private val lo: LifecycleOwner) : RecyclerView.Adapter<ExtFilesListAdapter.ExtFilesListHolder>() {

    private var listExtFiles = emptyList<ReserveExtFileData>()

    class ExtFilesListHolder(itemBinding: ExtFileItemBinding): RecyclerView.ViewHolder(itemBinding.root){
        val extFileName = itemBinding.extFileName
        val extFileImage = itemBinding.extFileImage
    }

    override fun onViewAttachedToWindow(holder: ExtFilesListHolder) {
        holder.itemView.setOnClickListener {
            ExtFilesListFragment.clickOnListItem(listExtFiles[holder.adapterPosition])
        }
        holder.itemView.setOnLongClickListener {
            ExtFilesListFragment.longClickOnListItem(listExtFiles[holder.adapterPosition], lo)
            true
        }
        super.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: ExtFilesListHolder) {
        holder.itemView.setOnClickListener(null)
        holder.itemView.setOnLongClickListener(null)
        super.onViewDetachedFromWindow(holder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExtFilesListHolder {

        return ExtFilesListHolder(
            ExtFileItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ExtFilesListHolder, position: Int) {
        val currentItem = listExtFiles[position]
        holder.extFileName.text = trimFileName(currentItem.fileName)
        holder.extFileImage.setExtFileImage(currentItem.getParamsBundle())
    }

    private fun trimFileName(fileName: String): String {
        val maxLength = 100
        if (fileName.length > maxLength) return fileName.substring(0,maxLength-8) + ".." + fileName.dropLast(5)
            else return fileName
    }

    override fun getItemCount(): Int = listExtFiles.size

    fun setData(extFiles:List<ReserveExtFileData>){
        listExtFiles = extFiles
        notifyDataSetChanged()

    }
}