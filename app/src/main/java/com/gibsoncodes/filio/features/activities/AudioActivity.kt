package com.gibsoncodes.filio.features.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.gibsoncodes.filio.R
import com.gibsoncodes.filio.adapters.AudiosAdapter
import com.gibsoncodes.filio.commons.RvItemTouchHelper
import com.gibsoncodes.filio.databinding.ActivityAudioBinding
import com.gibsoncodes.filio.features.fragments.OptionsBottomSheetFragment
import com.gibsoncodes.filio.features.viewmodels.AudioViewModel
import com.gibsoncodes.filio.models.AudiosModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

@Suppress("DEPRECATION")
class AudioActivity : BaseActivity(), OptionsBottomSheetFragment.ItemClickListener {
    private val binding by bind<ActivityAudioBinding>(R.layout.activity_audio)
    private val audioAdapter by lazy {
        AudiosAdapter ()
    }
    private var audioModel:AudiosModel?=null
    private val deleteRequestCode =0x101
    private val audioViewModel: AudioViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            lifecycleOwner = this@AudioActivity
            viewModel = audioViewModel
            activity = this@AudioActivity
            adapter = audioAdapter
            audiosRv.apply {
                val layoutManager = LinearLayoutManager(this@AudioActivity)
                layoutManager.orientation = LinearLayoutManager.VERTICAL
                this.layoutManager = layoutManager
                this.addOnItemTouchListener(RvItemTouchHelper(this@AudioActivity, object:RvItemTouchHelper.ListenerInterface{
                    override fun onClick(view: View, position: Int) {
                       audioModel = audioAdapter.currentList[position]
                        supportFragmentManager.let{
                            val bundle= Bundle().apply{
                                this.putString("name", audioModel?.name)
                                audioModel?.dateAdded?.time?.let { it1 ->
                                    this.putLong("dateAdded",
                                        it1
                                    )
                                }
                                audioModel?.size?.let { it1 -> this.putInt("size", it1) }
                            }
                            OptionsBottomSheetFragment.newInstance(bundle).apply {
                                show(it, "Fragment options tag")
                            }
                        }
                    }

                    override fun onLongClick(view: View, position: Int) {

                    }
                }))
            }
        }
        audioViewModel.permissionNeededToDelete.observe(this, Observer {
            intent->
            startIntentSenderForResult(intent,deleteRequestCode, null,0,0,0,null)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
       if (requestCode == deleteRequestCode && resultCode == Activity.RESULT_OK){
           audioViewModel.deletePendingAudio()
       }
    }
    private fun deleteAudio(audio: AudiosModel){
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.delete_dialog_title))
            .setMessage(getString(R.string.delete_dialog_message, audio.name))
            .setPositiveButton(R.string.delete_dialog_positive_message){_,_
                ->
                audioViewModel.deleteAudio(audio)
            }
            .setNegativeButton(R.string.delete_dialog_negative){dialog,_->
                dialog.dismiss()
            }
            .show()
    }
    override fun onItemClick(item: String) {
        when(item){
            "view"->{
                audioModel?.let{
                    val intent = Intent(Intent.ACTION_VIEW, it.uri)
                    val chooser = Intent.createChooser(intent, getString(R.string.view_with))
                    if (chooser.resolveActivity(packageManager)!=null){
                        startActivity(chooser)
                    }
                }
            }
            "delete"->{
                audioModel?.let{
                    deleteAudio(it)
                }
            }
        }
    }
}