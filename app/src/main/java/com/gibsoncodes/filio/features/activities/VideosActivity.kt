package com.gibsoncodes.filio.features.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.gibsoncodes.filio.R
import com.gibsoncodes.filio.adapters.VideosAdapter
import com.gibsoncodes.filio.commons.GridItemDecoration
import com.gibsoncodes.filio.commons.RvItemTouchHelper
import com.gibsoncodes.filio.commons.convertToPixels
import com.gibsoncodes.filio.databinding.ActivityVideosBinding
import com.gibsoncodes.filio.features.fragments.OptionsBottomSheetFragment
import com.gibsoncodes.filio.features.viewmodels.VideosViewModel
import com.gibsoncodes.filio.models.VideosModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

@Suppress("DEPRECATION")
class VideosActivity : BaseActivity(), OptionsBottomSheetFragment.ItemClickListener {
    private val binding:ActivityVideosBinding by bind(R.layout.activity_videos)
    private val videosViewModel: VideosViewModel by viewModel()
    private val downloadVideoRequestCode = 0x121
    private val videosAdapter by lazy {
        VideosAdapter()}
private var videosModel:VideosModel?=null
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
                this.addOnItemTouchListener(RvItemTouchHelper(this@VideosActivity,object:RvItemTouchHelper.ListenerInterface{
                    override fun onClick(view: View, position: Int) {
                        videosModel = videosAdapter.currentList[position]
                        supportFragmentManager.let {
                            val bundle= Bundle().apply{
                                this.putString("name", videosModel?.name)
                                videosModel?.dateAdded?.time?.let { it1 ->
                                    this.putLong("dateAdded",
                                        it1
                                    )
                                }
                                videosModel?.size?.let { it1 -> this.putInt("size", it1) }
                            }
                            OptionsBottomSheetFragment.newInstance(bundle)
                                .apply {
                                    show(it, "Fragment options tag") }}
                    }
                    override fun onLongClick(view: View, position: Int) {

                    }
                }))
            }
            adapter = videosAdapter

        }
        videosViewModel.permissionNeededToDelete.observe(this, Observer {
            it?.let{
                startIntentSenderForResult(it,downloadVideoRequestCode,
                null,0,0,0,null)
            }
        })

        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Activity.RESULT_OK && resultCode == downloadVideoRequestCode){
            videosViewModel.deletePendingVideoModel()
        }
    }
    private fun deleteVideo(video: VideosModel){
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.delete_dialog_title))
            .setMessage(getString(R.string.delete_dialog_message, video.name))
            .setPositiveButton(R.string.delete_dialog_positive_message){_,_
                ->
                videosViewModel.deleteVideo(video)
            }
            .setNegativeButton(R.string.delete_dialog_negative){dialog,_->
                dialog.dismiss()
            }
            .show()
    }
    override fun onItemClick(item: String) {
      when(item){
          "delete"->{
              videosModel?.let{
                  deleteVideo(it)
              }
          }
          "view"->{
              videosModel?.let {
                  val intent = Intent(Intent.ACTION_VIEW, it.uri)
                  val chooser = Intent.createChooser(intent, getString(R.string.view_with))
                  if (chooser.resolveActivity(packageManager) != null) {
                      startActivity(chooser)
                  }
              }
          }
      }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Intent(this@VideosActivity, MainActivity::class.java)
    }
}
