package com.gibsoncodes.filio.features.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gibsoncodes.filio.R
import com.gibsoncodes.filio.databinding.FragmentAboutBinding
import org.koin.android.ext.android.bind


class AboutFragment : BaseFragments() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = bindFragment<FragmentAboutBinding>(container,
        inflater, R.layout.fragment_about,
        false)
        // Inflate the layout for this fragment
        return binding.root
    }


}