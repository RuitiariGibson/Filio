package com.gibsoncodes.filio.features.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.gibsoncodes.filio.R
import com.gibsoncodes.filio.adapters.DownloadsAdapter
import com.gibsoncodes.filio.commons.RvItemTouchHelper
import com.gibsoncodes.filio.databinding.ActivityDownloadsBinding
import com.gibsoncodes.filio.features.fragments.OptionsBottomSheetFragment
import com.gibsoncodes.filio.features.viewmodels.DownloadsViewModel
import com.gibsoncodes.filio.models.DownloadsModel
import com.gibsoncodes.filio.models.VideosModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.bottom_sheet_options.*
import org.koin.androidx.viewmodel.ext.android.viewModel

@Suppress("DEPRECATION")
class DownloadActivity : BaseActivity() , OptionsBottomSheetFragment.ItemClickListener{
    private val binding by bind<ActivityDownloadsBinding>(R.layout.activity_downloads)
    private val downloadsAdapter by lazy {
        DownloadsAdapter () }
    private val viewModel: DownloadsViewModel by viewModel()
    private var pendingModel:DownloadsModel?=null
    private val deleteRequestCode = 0x106
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
                this.addOnItemTouchListener(RvItemTouchHelper(this@DownloadActivity,
                object:RvItemTouchHelper.ListenerInterface{
                    override fun onClick(view: View, position: Int) {
                        pendingModel = downloadsAdapter.currentList[position]
                        supportFragmentManager.let {
                            val bundle= Bundle().apply{
                                this.putString("name", pendingModel?.fileName)
                                pendingModel?.dateAdded?.time?.let { it1 ->
                                    this.putLong("dateAdded",it1)
                                }
                                pendingModel?.fileSize?.let { it1 -> this.putInt("size", it1) }
                            }
                            OptionsBottomSheetFragment.newInstance(bundle)
                                .apply {
                                    show(it,"Fragment options tag" )
                                }
                        }
                    }
                    override fun onLongClick(view: View, position: Int) {
                    }
                }))
            }
            adapter = downloadsAdapter
        }
        viewModel.permissionNeededToDelete.observe(this, Observer {
            it?.let{
                startIntentSenderForResult(it,deleteRequestCode,null,0,0,0,null)
            }
        })
    }
    private fun deleteDownloadFile(file: DownloadsModel){
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.delete_dialog_title))
            .setMessage(getString(R.string.delete_dialog_message, file.fileName))
            .setPositiveButton(R.string.delete_dialog_positive_message){_,_
                ->
                viewModel.deleteDownloadFile(file)
            }
            .setNegativeButton(R.string.delete_dialog_negative){dialog,_->
                dialog.dismiss()
            }
            .show()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == deleteRequestCode && resultCode== Activity.RESULT_OK){
        viewModel.deletePendingDownloadFile()
    }
    }

    override fun onItemClick(item: String) {
        when(item){
            "delete"->{
                pendingModel?.let{
                    deleteDownloadFile(it)
                }
            }
            "view"->{
                pendingModel?.let{
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