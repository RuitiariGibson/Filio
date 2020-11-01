package com.gibsoncodes.filio.features.activities

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.gibsoncodes.filio.R
import com.gibsoncodes.filio.adapters.AudiosAdapter
import com.gibsoncodes.filio.databinding.ActivityAudioBinding
import com.gibsoncodes.filio.features.viewmodels.AudioViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AudioActivity : BaseActivity() {
    private val binding by bind<ActivityAudioBinding>(R.layout.activity_audio)
    private val audioAdapter by lazy {
        AudiosAdapter ()
    }
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
            }
        }
    }
}