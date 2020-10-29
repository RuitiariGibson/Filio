package com.gibsoncodes.filio.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.gibsoncodes.filio.R
import com.gibsoncodes.filio.databinding.StorageCategoryItemLayoutBinding
import com.gibsoncodes.filio.models.CategoriesModel

class CategoriesAdapter:GenericAdapter<CategoriesModel,
        StorageCategoryItemLayoutBinding> (CategoriesModel.diffUtil){
    init{
        setHasStableIds(true)
    }
    override fun createBinding(parent: ViewGroup): StorageCategoryItemLayoutBinding {
        return DataBindingUtil.inflate<StorageCategoryItemLayoutBinding>(LayoutInflater.from(parent.context),
        R.layout.storage_category_item_layout, parent, false)
    }

    override fun bind(binding: StorageCategoryItemLayoutBinding, item: CategoriesModel) {
        binding.storageCategory = item

    }

}