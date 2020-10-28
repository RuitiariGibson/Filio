package com.gibsoncodes.filio.features.activities

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.gibsoncodes.filio.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private val readStorageRequestCode = 0x100
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*viewModel.downloadData.observe(this, Observer {
            /*var count =0
            it.forEach { file->
                count +=file.fileSize
            }
            val size=Formatter.formatFileSize(this, count.toLong())
            countTextView.text = "the size is: $size"*/
           // textView.text =it.joinToString (limit = 5)

        })
          viewModel.loadStorageStats()
        viewModel.usedUp.observe(this, Observer {
            used.text = it
        })
        viewModel.totalMemory.observe(this, Observer {
            total.text = it
        })*/
      //  if (havePermission()){
        //    used.text = "We have permission "
        //}else {
          //  requestPermission()
        // }


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
            // do the work
        }else{
            if (shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)){
                MaterialAlertDialogBuilder(this)
                    .setTitle("Permission")
                    .setMessage("We need to access read your storage in order for the app to function")
                    .setPositiveButton("Grant"){p0,p1->
                        requestPermission()
                    }.setNegativeButton("Cancel") {p0, p1->
                        p0.dismiss()
                    }
                    .show()
            }else{
                return
            }
        }
    }
}