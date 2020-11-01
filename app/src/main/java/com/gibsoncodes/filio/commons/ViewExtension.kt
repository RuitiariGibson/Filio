package com.gibsoncodes.filio.commons

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.util.TypedValue
import android.view.View
import android.view.animation.AlphaAnimation
import com.gibsoncodes.filio.R

 fun Context.convertToPixels(dp:Int):Int{
    val resources = resources
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
    dp.toFloat(),resources.displayMetrics).toInt()
}
 fun View.fadeIn(halfTime:Long =0L, fadeDuration:Long){
    this.visibility= View.INVISIBLE
    Handler().postDelayed({this.startAnimation(AlphaAnimation(0F,1F).apply{
        duration=fadeDuration
        fillAfter=true
    })
        this.visibility= View.VISIBLE}, halfTime)


}
/**
 * We need to load the file icons based on their extensions
 * if the uri cannot point to the file thumbnail hence these act as a back-up
 * The file icons are less than 1mb thus there is no
 * need to downscale the images
 */
 fun Context.loadThumbnail(extension:String): Bitmap {
    val bitmap: Bitmap?
    when(extension){
        "docx"->{
            bitmap = BitmapFactory.decodeResource(
                resources, R.drawable.ms_word_icon)
        }
        "ppt"->{
            bitmap = BitmapFactory.decodeResource(resources, R.drawable.power_point_file_icon)
        }
        "zip"->{
            bitmap = BitmapFactory.decodeResource(resources, R.drawable.zip_folder_icon)
        }
        "mp3","ogg","wav"->{
            bitmap = BitmapFactory.decodeResource(resources, R.drawable.audio_folder_icon)
        }
        "mpeg","mp4"->{
            bitmap = BitmapFactory.decodeResource(resources, R.drawable.videos_folder_icon)
        }
        "jpg","png","jpeg"->{
            bitmap = BitmapFactory.decodeResource(resources, R.drawable.picture_folder_icon)
        }
        else->{
            bitmap = BitmapFactory.decodeResource(resources, R.drawable.default_file_icon)
        }
    }
    return bitmap
}