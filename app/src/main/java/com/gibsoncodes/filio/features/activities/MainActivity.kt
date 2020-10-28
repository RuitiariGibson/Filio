package com.gibsoncodes.filio.features.activities

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.format.Formatter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.gibsoncodes.filio.R
import com.gibsoncodes.filio.features.viewmodels.FileSizesViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {
    private val readStorageRequestCode = 0x100
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel:FileSizesViewModel by viewModel()
        viewModel.fileSizeLiveData.observe(this, Observer {
            //  it.joinToString(prefix = "[", postfix = "]")
          val string_= Formatter.formatFileSize(this@MainActivity, it[3].categorySize.toLong())
            testText.text = "${it[3].categoryName} is $string_"
        })
      //  if (havePermission()){
        //    used.text = "We have permission "
        //}else { download is 0.97 gb
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