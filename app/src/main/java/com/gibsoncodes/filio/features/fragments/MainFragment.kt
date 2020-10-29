package com.gibsoncodes.filio.features.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gibsoncodes.filio.R
import com.gibsoncodes.filio.adapters.CategoriesAdapter
import com.gibsoncodes.filio.databinding.FragmentMainBinding
import com.gibsoncodes.filio.features.viewmodels.FileSizesViewModel
import com.gibsoncodes.filio.features.viewmodels.StorageStatisticsViewModel
import com.gibsoncodes.filio.models.StorageStatisticsModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainFragment : BaseFragments() {
    private  val categoriesAdapter by lazy { CategoriesAdapter() }
    private val storageStatisticsViewModel:StorageStatisticsViewModel by viewModel()
    private val fileSizeViewMode:FileSizesViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = bindFragment<FragmentMainBinding>(container,
         inflater, R.layout.fragment_main, false)
        binding.apply {
            this.lifecycleOwner=viewLifecycleOwner
            adapter =categoriesAdapter
            this.viewModel = fileSizeViewMode
            this.storageStatistics = storageStatisticsViewModel
        }
        return binding.root
    }



}