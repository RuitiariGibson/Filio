package com.gibsoncodes.filio.adapters

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class GenericViewHolder <out T:ViewDataBinding>(val binding:T):RecyclerView.ViewHolder(binding.root)