package com.gibsoncodes.filio.features.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.gibsoncodes.filio.R
import com.gibsoncodes.filio.adapters.CategoriesAdapter
import com.gibsoncodes.filio.adapters.RecentFilesAdapter
import com.gibsoncodes.filio.commons.RvItemTouchHelper
import com.gibsoncodes.filio.commons.ZoomEffectRv
import com.gibsoncodes.filio.commons.fadeIn
import com.gibsoncodes.filio.databinding.FragmentMainBinding
import com.gibsoncodes.filio.features.activities.AudioActivity
import com.gibsoncodes.filio.features.activities.DownloadActivity
import com.gibsoncodes.filio.features.activities.ImagesActivity
import com.gibsoncodes.filio.features.activities.VideosActivity
import com.gibsoncodes.filio.features.viewmodels.FileSizesViewModel
import com.gibsoncodes.filio.features.viewmodels.RecentFilesViewModel
import com.gibsoncodes.filio.features.viewmodels.StorageStatisticsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainFragment : BaseFragments() {
    /**
     * TODO: find a suitable fragment-activity transition
     * Explode transition is laggy due to the recycler view items
     */

    private val categoriesAdapter by lazy { CategoriesAdapter() }
    private val storageStatisticsViewModel: StorageStatisticsViewModel by viewModel()
    private val fileSizeViewMode: FileSizesViewModel by viewModel()
    private val recentFilesAdapter by lazy {
        RecentFilesAdapter()
    }
    private lateinit var binding: FragmentMainBinding
    private val recentFilesViewModel: RecentFilesViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
         binding = bindFragment<FragmentMainBinding>(container,
         inflater, R.layout.fragment_main, false)
        binding.apply {
            this.lifecycleOwner=viewLifecycleOwner
            this.viewModel = fileSizeViewMode
            this.storageStatistics = storageStatisticsViewModel
            this.recentFilesVm = recentFilesViewModel
            this.storageCategoriesRv.apply{
                this.layoutManager = LinearLayoutManager(requireContext()).also { it.orientation=LinearLayoutManager.VERTICAL }
                this.addOnItemTouchListener(RvItemTouchHelper(requireContext(),object:RvItemTouchHelper.ListenerInterface{
                    override fun onClick(view: View, position: Int) {
                        val clickedItem = categoriesAdapter.currentList[position]
                        when(clickedItem.categoryName){
                            "Audio"->{
                                startActivity(Intent(requireContext(),AudioActivity::class.java))
                            }
                            "Videos"->{
                                startActivity(Intent(requireContext(), VideosActivity::class.java))
                            }
                            "Downloads"->{
                                startActivity(Intent(requireContext(), DownloadActivity::class.java))
                            }
                            "Images"->{
                                startActivity(Intent(requireContext(), ImagesActivity::class.java))
                            }
                        }
                    }

                    override fun onLongClick(view: View, position: Int) {
                            Toast.makeText(requireContext(),
                                "${categoriesAdapter.currentList[position].categoryName} long clicked",
                                Toast.LENGTH_LONG).show()
                    }
                }))
            }
            this.recentFilesRv.apply{
                val layoutManager = ZoomEffectRv(requireContext())
                layoutManager.orientation = LinearLayoutManager.HORIZONTAL
                this.layoutManager =layoutManager
            }
            adapter =categoriesAdapter
            this.recentAdapter = recentFilesAdapter

        }
        (activity as? AppCompatActivity)?.setSupportActionBar(binding.myStorage)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val constantDuration =2500L
        binding.myStorage.fadeIn(fadeDuration = constantDuration)
        binding.firstLayout.fadeIn(halfTime = 1.5.toLong()* constantDuration, fadeDuration = constantDuration)
        binding.recentFilesRv.fadeIn(halfTime = 2* constantDuration, fadeDuration = constantDuration)
        binding.categoriesText.fadeIn(halfTime = 2.5.toLong()*constantDuration, fadeDuration = constantDuration)
        binding.storageCategoriesRv.fadeIn(3*constantDuration, fadeDuration = constantDuration)
    }

}