package com.gibsoncodes.filio.features.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

@Suppress("DEPRECATION")
class ImagesActivity : BaseActivity(), OptionsBottomSheetFragment.ItemClickListener{
    private val DELETE_PERMISSION_REQUEST = 0x1033
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

viewModel.permissionNeededToDelete.observe(this, Observer {
    intentSender->
    intentSender?.let{
        /**
         * Needed for android 10+
         */
        startIntentSenderForResult(intentSender,
        DELETE_PERMISSION_REQUEST,null,0,0,0,null)
    }
})
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
       if (resultCode == Activity.RESULT_OK && requestCode == DELETE_PERMISSION_REQUEST){
           viewModel.deletePendingImage()
       }
    }
    override fun onItemClick(item: String) {
        when(item){
            "delete"->{
            // perform delete action
                imageModel?.let{
                      deleteImage(it)
                }
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
    private fun deleteImage(image:ImagesModel){
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.delete_dialog_title))
            .setMessage(getString(R.string.delete_dialog_message, image.name))
            .setPositiveButton(R.string.delete_dialog_positive_message){_,_
            ->
                viewModel.deleteImage(image)
            }
            .setNegativeButton(R.string.delete_dialog_negative){dialog,_->
                dialog.dismiss()
            }
            .show()
    }
}