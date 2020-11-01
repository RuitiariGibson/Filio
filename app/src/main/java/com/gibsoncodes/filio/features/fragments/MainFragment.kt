package com.gibsoncodes.filio.features.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.gibsoncodes.filio.R
import com.gibsoncodes.filio.adapters.CategoriesAdapter
import com.gibsoncodes.filio.adapters.RecentFilesAdapter
import com.gibsoncodes.filio.commons.RvItemTouchHelper
import com.gibsoncodes.filio.commons.fadeIn
import com.gibsoncodes.filio.databinding.FragmentMainBinding
import com.gibsoncodes.filio.features.viewmodels.FileSizesViewModel
import com.gibsoncodes.filio.features.viewmodels.RecentFilesViewModel
import com.gibsoncodes.filio.features.viewmodels.StorageStatisticsViewModel
import com.gibsoncodes.filio.models.StorageStatisticsModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainFragment : BaseFragments() {
    private  val categoriesAdapter by lazy { CategoriesAdapter() }
    private val storageStatisticsViewModel:StorageStatisticsViewModel by viewModel()
    private val fileSizeViewMode:FileSizesViewModel by viewModel()
    private val recentFilesAdapter by lazy{
        RecentFilesAdapter()
    }
    private lateinit var binding:FragmentMainBinding
    private val recentFilesViewModel:RecentFilesViewModel by viewModel()

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
                        Toast.makeText(requireContext(),
                            "${categoriesAdapter.currentList[position].categoryName} clicked",
                        Toast.LENGTH_LONG).show()
                    }

                    override fun onLongClick(view: View, position: Int) {
                            Toast.makeText(requireContext(),
                                "${categoriesAdapter.currentList[position].categoryName} long clicked",
                                Toast.LENGTH_LONG).show()
                    }
                }))
            }
            this.recentFilesRv.apply{
                this.layoutManager = LinearLayoutManager(requireContext()).also{it.orientation=LinearLayoutManager.HORIZONTAL}
            }
            adapter =categoriesAdapter
            this.recentAdapter = recentFilesAdapter

        }
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