package com.gibsoncodes.filio.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.gibsoncodes.filio.R
import com.gibsoncodes.filio.databinding.RecentFilesItemBinding
import com.gibsoncodes.filio.models.RecentFilesModel

class RecentFilesAdapter:GenericAdapter<RecentFilesModel, RecentFilesItemBinding> (RecentFilesModel.diffUtil) {
    override fun bind(binding: RecentFilesItemBinding, item: RecentFilesModel) {
        binding.recentFiles = item
    }

    override fun createBinding(parent: ViewGroup): RecentFilesItemBinding {
        return DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.recent_files_item,
        parent, false)
    }
}