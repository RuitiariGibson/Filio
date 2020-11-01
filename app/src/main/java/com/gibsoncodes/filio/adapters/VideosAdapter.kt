package com.gibsoncodes.filio.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.gibsoncodes.filio.R
import com.gibsoncodes.filio.databinding.VideosItemLayoutBinding
import com.gibsoncodes.filio.models.VideosModel

class VideosAdapter:GenericAdapter<VideosModel, VideosItemLayoutBinding>(diffUtilVideos) {

    override fun bind(binding: VideosItemLayoutBinding, item: VideosModel) {
        binding.videosModel = item
    }

    override fun createBinding(parent: ViewGroup): VideosItemLayoutBinding {
        val binding = DataBindingUtil.inflate<VideosItemLayoutBinding>(
            LayoutInflater.from(parent.context),
            R.layout.videos_item_layout, parent, false
        )

        return binding
    }

    companion object {
        val diffUtilVideos = object : DiffUtil.ItemCallback<VideosModel>() {
            override fun areContentsTheSame(oldItem: VideosModel, newItem: VideosModel): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areItemsTheSame(oldItem: VideosModel, newItem: VideosModel): Boolean {
                return oldItem.id == newItem.id
            }
        }

    }

}