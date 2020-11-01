package com.gibsoncodes.filio.features.activities

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.gibsoncodes.filio.R
import com.gibsoncodes.filio.adapters.VideosAdapter
import com.gibsoncodes.filio.commons.GridItemDecoration
import com.gibsoncodes.filio.commons.convertToPixels
import com.gibsoncodes.filio.databinding.ActivityVideosBinding
import com.gibsoncodes.filio.features.viewmodels.VideosViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class VideosActivity : BaseActivity() {
    private val binding:ActivityVideosBinding by bind(R.layout.activity_videos)
    private val videosViewModel: VideosViewModel by viewModel()
    private val videosAdapter by lazy {
        VideosAdapter()}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            lifecycleOwner=this@VideosActivity
            viewModel = videosViewModel
            activity = this@VideosActivity
            videosRv.apply {
                val layoutManager = GridLayoutManager(this@VideosActivity, 2)
                layoutManager.orientation = GridLayoutManager.VERTICAL
                this.layoutManager = layoutManager
                this.addItemDecoration(GridItemDecoration(true, convertToPixels(10), 2))
            }
            adapter = videosAdapter

        }
        }
    }
