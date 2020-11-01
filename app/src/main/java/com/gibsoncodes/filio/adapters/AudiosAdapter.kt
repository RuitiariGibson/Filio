package com.gibsoncodes.filio.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.gibsoncodes.filio.R
import com.gibsoncodes.filio.databinding.AudiosItemLayoutBinding
import com.gibsoncodes.filio.models.AudiosModel

class AudiosAdapter:GenericAdapter<AudiosModel, AudiosItemLayoutBinding>(diffUtil) {
    init{
        setHasStableIds(true)
    }
companion object{
    val diffUtil = object:DiffUtil.ItemCallback<AudiosModel>(){

        override fun areContentsTheSame(oldItem: AudiosModel, newItem: AudiosModel): Boolean {
           return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: AudiosModel, newItem: AudiosModel): Boolean {
            return oldItem.id == newItem.id
        }
    }
}
    override fun createBinding(parent: ViewGroup): AudiosItemLayoutBinding {

        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.audios_item_layout, parent, false
        )


    }

    override fun bind(binding: AudiosItemLayoutBinding, item: AudiosModel) {
      binding.audioModel = item
    }

}