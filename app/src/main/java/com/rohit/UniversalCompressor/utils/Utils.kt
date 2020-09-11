package com.rohit.UniversalCompressor.utils

import android.content.Context
import android.widget.Toast

/**
 * Created by rohit on 9/12/20.
 */
object Utils {

    const val KEY_VIDEO_PATH= "ssid"

    fun showToast(context: Context,message:String){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
    }
}