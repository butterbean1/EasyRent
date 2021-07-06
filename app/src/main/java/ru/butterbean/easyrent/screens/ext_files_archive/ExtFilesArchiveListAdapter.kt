package ru.butterbean.easyrent.screens.ext_files_archive

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.butterbean.easyrent.databinding.ExtFileItemBinding
import ru.butterbean.easyrent.models.ReserveArchiveExtFileData
import ru.butterbean.easyrent.models.ReserveExtFileData
import ru.butterbean.easyrent.utils.setExtFileImage
import ru.butterbean.easyrent.utils.trimFileName

class ExtFilesArchiveListAdapter() : RecyclerView.Adapter<ExtFilesArchiveListAdapter.ExtFilesListHolder>() {

    private var listExtFiles = emptyList<ReserveArchiveExtFileData>()

    class ExtFilesListHolder(itemBinding: ExtFileItemBinding): RecyclerView.ViewHolder(itemBinding.root){
        val extFileName = itemBinding.extFileName
        val extFileImage = itemBinding.extFileImage
    }

    override fun onViewAttachedToWindow(holder: ExtFilesListHolder) {
        holder.itemView.setOnClickListener {
            ExtFilesArchiveListFragment.clickOnListItem(listExtFiles[holder.adapterPosition])
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

    override fun getItemCount(): Int = listExtFiles.size

    fun setData(extFiles:List<ReserveArchiveExtFileData>){
        listExtFiles = extFiles
        notifyDataSetChanged()

    }
}