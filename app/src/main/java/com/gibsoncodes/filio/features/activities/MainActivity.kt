package com.gibsoncodes.filio.features.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.view.View
import android.view.Window
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.gibsoncodes.filio.R
import com.gibsoncodes.filio.commons.AnimatorListener
import com.gibsoncodes.filio.commons.BottomNavigationBehaviour
import com.gibsoncodes.filio.databinding.ActivityMainBinding
import com.gibsoncodes.filio.features.fragments.AboutFragment
import com.gibsoncodes.filio.features.fragments.MainFragment
import com.gibsoncodes.filio.features.fragments.StorageStatsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.color.MaterialColors
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.transition.platform.MaterialArcMotion
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import java.util.*


class MainActivity : AppCompatActivity() {
    private val readStorageRequestCode = 0x100
    private val mainFragment = MainFragment()
    private val statisticsFragment = StorageStatsFragment()
    private val aboutFragment = AboutFragment()
    private var activeFragment: Fragment? = mainFragment
    private val fragManager = supportFragmentManager
    private lateinit var binding: ActivityMainBinding
    private val customAction = "com.gibsoncodes.filio.IMAGES_CUSTOM_ACTION"
    private val videosCustomAction = "com.gibsoncodes.filio.VIDEOS_CUSTOM_ACTION"
    private val audioCustomAction = "com.gibsoncodes.filio.AUDIO_CUSTOM_ACTION"
    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS) // we can manually set this in styles.xml or set in the activity
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.mainScreenCord.transitionName = "shared_element_end_root"
        setEnterSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        window.sharedElementEnterTransition = buildContainerTransform()
        window.sharedElementReturnTransition = buildContainerTransform()
        setContentView(binding.mainScreenCord)

        super.onCreate(savedInstanceState)
        binding.apply {
            lifecycleOwner = this@MainActivity
        }
        // setSupportActionBar(binding.toolBar)
        if (havePermission()) {
            binding.bottomNavigationView.fadeBottomNavigationView(false)
            doInitialFragmentTransaction()
            binding.bottomNavigationView.wireUpListener()
        }else{
            binding.bottomNavigationView.fadeBottomNavigationView(true)
         binding.permissionRationale.animate()
             .alpha(1f)
             .setDuration(80L)
             .setListener(AnimatorListener{
                 binding.permissionRationale.visibility=View.VISIBLE
                 binding.fragmentContainer.visibility = View.INVISIBLE
             })
            binding.grantPermissionButton.setOnClickListener {
                requestPermission()
            }
        }
        val layoutParams: CoordinatorLayout.LayoutParams = binding.bottomNavigationView
            .layoutParams as CoordinatorLayout.LayoutParams
        layoutParams.behavior = BottomNavigationBehaviour()
        binding.bottomNavigationView.wireUpListener()
        createDynamicShortcuts()
    }

    private fun havePermission():Boolean{
        return ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }
    private fun requestPermission(){
        if (!havePermission()){
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE),readStorageRequestCode)
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
                   binding.bottomNavigationView.fadeBottomNavigationView(false)
               })
            doInitialFragmentTransaction()
            binding.bottomNavigationView.wireUpListener()
        }else{
            if (binding.fragmentContainer.visibility==View.VISIBLE)
                binding.fragmentContainer.visibility=View.GONE

            if (shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)){
                MaterialAlertDialogBuilder(this)
                    .setTitle(getString(R.string.permission))
                    .setMessage(getString(R.string.permission_rationale))
                    .setPositiveButton(getString(R.string.grant)){_,_->
                        requestPermission()
                    }.setNegativeButton(getString(R.string.deny)) {p0, _->
                        binding.fragmentContainer.visibility=View.GONE
                        binding.permissionRationale.visibility=View.VISIBLE
                        binding.bottomNavigationView.fadeBottomNavigationView(true)
                        p0.dismiss()
                    }
                    .show()
            }else{
                goToSetting()
            }
        }
    }
    private fun goToSetting(){
        Intent(ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:$packageName")).apply {
            addCategory(Intent.CATEGORY_DEFAULT)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }.also{
            intent->
            startActivity(intent)
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
    private fun BottomNavigationView.fadeBottomNavigationView(fade:Boolean){
        when {
            fade -> {
                binding.bottomNavigationView.animate()
                    .alpha(0f)
                    .setDuration(2000L)
                    .setInterpolator(DecelerateInterpolator())
                    .setListener(AnimatorListener{
                        binding.bottomNavigationView.visibility=View.INVISIBLE
                    })
            }
            else -> {
                binding.bottomNavigationView.animate()
                    .alpha(1f)
                    .setDuration(2000L)
                    .setInterpolator(DecelerateInterpolator())
                    .setListener(AnimatorListener{
                        binding.bottomNavigationView.visibility=View.VISIBLE
                    })
            }
        }
    }

    private fun doInitialFragmentTransaction() {

        fragManager.beginTransaction().add(
            R.id.fragmentContainer,
            statisticsFragment, getString(R.string.statisticsFragTag)
        )
            .hide(statisticsFragment).commit()
        fragManager.beginTransaction().add(
            R.id.fragmentContainer,
            aboutFragment, getString(R.string.aboutFragTag)
        ).hide(aboutFragment).commit()
        fragManager.beginTransaction().add(
            R.id.fragmentContainer, mainFragment,
            getString(R.string.mainFragTag)
        ).commit()
    }

    private fun createDynamicShortcuts() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            val shortCutManager = getSystemService<ShortcutManager>(ShortcutManager::class.java)
            val imagesShortcut = ShortcutInfo.Builder(this, "shortCutId1")
                .setShortLabel("images")
                .setLongLabel("images")
                .setIcon(Icon.createWithResource(this, R.drawable.imagetype))
                .setIntent(Intent(this@MainActivity, ImagesActivity::class.java).also {
                    it.action = customAction
                })
                .build()
            val videosShortCut = ShortcutInfo.Builder(this, "shortCutId2")
                .setShortLabel("videos")
                .setLongLabel("videos")
                .setIcon(Icon.createWithResource(this, R.drawable.video_file_type))
                .setIntent(
                    Intent(this@MainActivity, VideosActivity::class.java)
                        .setAction(videosCustomAction)
                )
                .build()
            val audioShortcut = ShortcutInfo.Builder(this, "shortCutId3")
                .setShortLabel("audio")
                .setLongLabel("audio")
                .setIcon(Icon.createWithResource(this, R.drawable.audio_type))
                .setIntent(Intent(this@MainActivity, AudioActivity::class.java).also {
                    it.action = audioCustomAction
                })
                .build()
            shortCutManager!!.dynamicShortcuts =
                Arrays.asList(imagesShortcut, videosShortCut, audioShortcut)
        }
    }

    private fun buildContainerTransform() =
        MaterialContainerTransform().apply {
            addTarget(binding.mainScreenCord)
            setAllContainerColors(MaterialColors.getColor(binding.root, R.attr.colorSurface))
            pathMotion = MaterialArcMotion()
            duration = 1500
            interpolator = FastOutSlowInInterpolator()
            fadeMode = MaterialContainerTransform.FADE_MODE_IN
        }

    override fun onBackPressed() {
        if (activeFragment != null && activeFragment != mainFragment) {
            fragManager.beginTransaction()
                .hide(activeFragment!!)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .show(mainFragment)
                .commit()
            activeFragment = mainFragment
        } else {
            super.onBackPressed()
        }
    }
}