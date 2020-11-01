package com.gibsoncodes.filio.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.gibsoncodes.filio.R
import com.gibsoncodes.filio.databinding.DonwloadFilesLayoutBinding
import com.gibsoncodes.filio.models.DownloadsModel

class DownloadsAdapter:GenericAdapter<DownloadsModel, DonwloadFilesLayoutBinding>(difUtil) {
    companion object{
        val difUtil = object:DiffUtil.ItemCallback<DownloadsModel>(){
            override fun areContentsTheSame(
                oldItem: DownloadsModel,
                newItem: DownloadsModel
            ): Boolean {
                return oldItem.fileName == newItem.fileName
            }

            override fun areItemsTheSame(
                oldItem: DownloadsModel,
                newItem: DownloadsModel
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
    init{
        setHasStableIds(true)
    }
    override fun createBinding(parent: ViewGroup): DonwloadFilesLayoutBinding {

        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.donwload_files_layout, parent, false
        )
    }

    override fun bind(binding: DonwloadFilesLayoutBinding, item: DownloadsModel) {
        binding.downloadsModel = item    }
}