package com.gibsoncodes.filio.features.activities

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.gibsoncodes.filio.R
import com.gibsoncodes.filio.commons.AnimatorListener
import com.gibsoncodes.filio.databinding.ActivityMainBinding
import com.gibsoncodes.filio.features.fragments.AboutFragment
import com.gibsoncodes.filio.features.fragments.MainFragment
import com.gibsoncodes.filio.features.fragments.StorageStatsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class MainActivity : BaseActivity() {
    private val readStorageRequestCode = 0x100
    private val mainFragment = MainFragment()
   private val statisticsFragment = StorageStatsFragment()
    private val aboutFragment = AboutFragment()
    private var activeFragment:Fragment? = mainFragment
    private val fragManager = supportFragmentManager
    private val binding:ActivityMainBinding by bind(R.layout.activity_main)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (havePermission()){
                doInitialFragmentTransaction()
            binding.bottomNavigationView.wireUpListener()
        }else{
         binding.permissionRationale.animate()
             .alpha(1f)
             .setDuration(80L)
             .setListener(AnimatorListener{
                 binding.permissionRationale.visibility=View.VISIBLE
                 binding.fragmentContainer.visibility=View.INVISIBLE
             })
            binding.grantPermissionButton.setOnClickListener {
                requestPermission()
            }
        }


    }
    private fun havePermission():Boolean{
        return ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }
    private fun requestPermission(){
        if (!havePermission()){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),readStorageRequestCode)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode==readStorageRequestCode && grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
           binding.permissionRationale.animate()
               .alpha(0f)
               .setDuration(80L)
               .setListener(AnimatorListener{
                   binding.permissionRationale.visibility=View.GONE
                   binding.fragmentContainer.visibility=View.VISIBLE
               })
        }else{
            if (binding.fragmentContainer.visibility==View.VISIBLE)
                binding.fragmentContainer.visibility=View.GONE

            if (shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)){
                MaterialAlertDialogBuilder(this)
                    .setTitle(getString(R.string.permission))
                    .setMessage(getString(R.string.permission_rationale))
                    .setPositiveButton(getString(R.string.grant)){p0,p1->
                        requestPermission()
                    }.setNegativeButton(getString(R.string.deny)) {p0, p1->
                        binding.fragmentContainer.visibility=View.GONE
                        binding.permissionRationale.visibility=View.VISIBLE
                        p0.dismiss()
                    }
                    .show()
            }else{
                return
            }
        }
    }
private fun BottomNavigationView.wireUpListener(){
    this.setOnNavigationItemSelectedListener {
        when(it.itemId){
            R.id.bottom_navigation_home->{
                fragManager.beginTransaction()
                    .hide(activeFragment!!)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .show(mainFragment)
                    .commit()
                activeFragment=mainFragment
                true
            }
            R.id.bottom_navigation_stats->{
                fragManager.beginTransaction()
                    .hide(activeFragment!!)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .show(statisticsFragment)
                    .commit()
                activeFragment = statisticsFragment
                true
            }
            R.id.bottom_navigation_about->{
                fragManager.beginTransaction()
                    .hide(activeFragment!!)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .show(aboutFragment)
                    .commit()
                activeFragment=aboutFragment
                true
            }
            else->false
        }
    }
}
    private fun doInitialFragmentTransaction(){
        fragManager.beginTransaction().add(R.id.fragmentContainer,mainFragment,
            getString(R.string.mainFragTag)).show(mainFragment).commit()
        fragManager.beginTransaction().add(R.id.fragmentContainer,
            statisticsFragment,getString(R.string.statisticsFragTag))
            .hide(statisticsFragment)
        fragManager.beginTransaction().add(R.id.fragmentContainer,
            aboutFragment,getString(R.string.aboutFragTag)).hide(aboutFragment)
    }
    override fun onDestroy() {
        if (activeFragment!=null && activeFragment!=mainFragment){
            fragManager.beginTransaction()
                .hide(activeFragment!!)
                .show(mainFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit()
            activeFragment=mainFragment
        }else{
            super.onDestroy()
        }
    }
}