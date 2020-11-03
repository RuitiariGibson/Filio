package com.gibsoncodes.filio.features.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.gibsoncodes.filio.R
import com.gibsoncodes.filio.commons.fadeIn
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
            container, inflater, R.layout.fragment_storage_stats, false
        ).apply {
            this.lifecycleOwner = viewLifecycleOwner
            this.storageStatistics = storageStatisticsViewModel
        }
        /**
         * wrap it as an instance of AppCompatActivity in order to extend support
         */
        (activity as? AppCompatActivity)?.setSupportActionBar(binding.storageStats)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val constantDuration = 2500L
        binding.storageStatsCard.fadeIn(
            halfTime = 2.0.toLong() * constantDuration,
            fadeDuration = constantDuration
        )
    }
}