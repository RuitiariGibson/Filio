package com.gibsoncodes.filio.features.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.gibsoncodes.filio.R
import com.gibsoncodes.filio.adapters.ImagesAdapter
import com.gibsoncodes.filio.commons.GridItemDecoration
import com.gibsoncodes.filio.commons.RvItemTouchHelper
import com.gibsoncodes.filio.commons.convertToPixels
import com.gibsoncodes.filio.databinding.ActivityImagesBinding
import com.gibsoncodes.filio.features.fragments.OptionsBottomSheetFragment
import com.gibsoncodes.filio.features.viewmodels.ImagesViewModel
import com.gibsoncodes.filio.models.ImagesModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ImagesActivity : BaseActivity(), OptionsBottomSheetFragment.ItemClickListener{
    private val binding by bind<ActivityImagesBinding>(R.layout.activity_images)
    private val imagesAdapter: ImagesAdapter by lazy {
        ImagesAdapter()
    }
    private val viewModel: ImagesViewModel by viewModel()
    private var imageModel :ImagesModel ? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            lifecycleOwner = this@ImagesActivity

            imagesViewModel = viewModel
            activity = this@ImagesActivity
            imagesRecyclerView.apply {
                val layoutManager = GridLayoutManager(this@ImagesActivity, 2)
                layoutManager.orientation = GridLayoutManager.VERTICAL
                this.layoutManager = layoutManager
                this.addItemDecoration(GridItemDecoration(true, convertToPixels(8), 2))
                this.addOnItemTouchListener(RvItemTouchHelper(this@ImagesActivity, object: RvItemTouchHelper.ListenerInterface{
                    override fun onClick(view: View, position: Int) {
                        supportFragmentManager.let{
                            OptionsBottomSheetFragment.newInstance(Bundle()).apply {
                                show(it, getString(R.string.bottom_options_frag_tag))
                            }
                        }
                        imageModel =imagesAdapter.currentList[position]
                    }

                    override fun onLongClick(view: View, position: Int) {

                    }
                }))
        }
            adapter= imagesAdapter
    }


    }

    override fun onItemClick(item: String) {
        when(item){
            "delete"->{
            // perform delete action
            }
            "view"->{
               imageModel?.let{
                   val intent = Intent(Intent.ACTION_VIEW, it.uri)
                   val chooser = Intent.createChooser(intent, getString(R.string.view_with))
                   if (chooser.resolveActivity(packageManager)!=null){
                       startActivity(chooser)
                   }
               }
            }
        }
    }
}