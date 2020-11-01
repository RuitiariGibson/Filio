package com.gibsoncodes.filio.features.activities

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.gibsoncodes.filio.R
import com.gibsoncodes.filio.adapters.ImagesAdapter
import com.gibsoncodes.filio.commons.GridItemDecoration
import com.gibsoncodes.filio.commons.convertToPixels
import com.gibsoncodes.filio.databinding.ActivityImagesBinding
import com.gibsoncodes.filio.features.viewmodels.ImagesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ImagesActivity : BaseActivity() {
    private val binding by bind<ActivityImagesBinding>(R.layout.activity_images)
    private val imagesAdapter: ImagesAdapter by lazy {
        ImagesAdapter()
    }
    private val viewModel: ImagesViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            lifecycleOwner = this@ImagesActivity
            adapter= imagesAdapter
            imagesViewModel = viewModel
            activity = this@ImagesActivity
            imagesRecyclerView.apply {
                val layoutManager = GridLayoutManager(this@ImagesActivity, 2)
                layoutManager.orientation = GridLayoutManager.VERTICAL
                this.layoutManager = layoutManager
                this.addItemDecoration(GridItemDecoration(true, convertToPixels(10), 2))
        }
    }

}
}