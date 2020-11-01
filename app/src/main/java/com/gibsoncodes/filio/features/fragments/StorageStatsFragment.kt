package com.gibsoncodes.filio.features.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gibsoncodes.di.modules.storageStatisticsModule
import com.gibsoncodes.filio.R
import com.gibsoncodes.filio.databinding.FragmentStorageStatsBinding
import com.gibsoncodes.filio.features.viewmodels.StorageStatisticsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class StorageStatsFragment : BaseFragments() {

private val storageStatisticsViewModel:StorageStatisticsViewModel by viewModel()
    private lateinit var binding:FragmentStorageStatsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         binding = bindFragment<FragmentStorageStatsBinding>(
            container, inflater,R.layout.fragment_storage_stats, false
        ).apply {
            this.lifecycleOwner=viewLifecycleOwner
            this.storageStatistics= storageStatisticsViewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.storageStats.alpha=0f
        binding.storageStats.animate()
            .translationY(binding.storageStats.height.toFloat())
            .alpha(1.0f)


    }
}