package com.gibsoncodes.filio.models

import androidx.recyclerview.widget.DiffUtil

data class CategoriesModel(val categoryName:String,val  categorySize:Int){
    companion object{
        val diffUtil = object: DiffUtil.ItemCallback<CategoriesModel> (){
            override fun areItemsTheSame(
                oldItem: CategoriesModel,
                newItem: CategoriesModel
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: CategoriesModel,
                newItem: CategoriesModel
            ): Boolean {
                return oldItem.categoryName == newItem.categoryName
            }
        }
    }
}
