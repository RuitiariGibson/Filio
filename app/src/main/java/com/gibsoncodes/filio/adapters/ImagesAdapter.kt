package com.gibsoncodes.filio.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.gibsoncodes.filio.R
import com.gibsoncodes.filio.databinding.ImagesItemLayoutBinding
import com.gibsoncodes.filio.models.ImagesModel

class ImagesAdapter:GenericAdapter<ImagesModel,
        ImagesItemLayoutBinding>(diffUtil) {
    companion object{
        val diffUtil = object:DiffUtil.ItemCallback<ImagesModel>(){
            override fun areContentsTheSame(oldItem: ImagesModel, newItem: ImagesModel): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: ImagesModel, newItem: ImagesModel): Boolean {
                return oldItem.uri == newItem.uri
            }
        }
    }
    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun bind(binding: ImagesItemLayoutBinding, item: ImagesModel) {
        binding.imagesModel = item
    }

    override fun createBinding(parent: ViewGroup): ImagesItemLayoutBinding {
        val binding = DataBindingUtil.inflate<ImagesItemLayoutBinding>(
            LayoutInflater.from(parent.context),
            R.layout.images_item_layout,
            parent, false
        )

        return binding
    }
}
