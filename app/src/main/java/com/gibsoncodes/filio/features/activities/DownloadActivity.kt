package com.gibsoncodes.filio.features.activities

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.gibsoncodes.filio.R
import com.gibsoncodes.filio.adapters.DownloadsAdapter
import com.gibsoncodes.filio.databinding.ActivityDownloadsBinding
import com.gibsoncodes.filio.features.viewmodels.DownloadsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DownloadActivity : BaseActivity() {
    private val binding by bind<ActivityDownloadsBinding>(R.layout.activity_downloads)
    private val downloadsAdapter by lazy {
        DownloadsAdapter () }
    private val viewModel: DownloadsViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            activity = this@DownloadActivity
            lifecycleOwner = this@DownloadActivity
            downloadFilesViewModel = viewModel
            downloadsRv.apply {
                val layoutManager = LinearLayoutManager(this@DownloadActivity)
                this.layoutManager =
                    layoutManager.also { it.orientation = LinearLayoutManager.VERTICAL }
            }
            adapter = downloadsAdapter

        }
    }
}