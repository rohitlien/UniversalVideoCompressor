package com.rohit.UniversalCompressor.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.rohit.UniversalCompressor.R

class SplashActivity : AppCompatActivity() {

    private var handler:Handler?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        handler = Handler()
        handler?.postDelayed(Runnable {
            launchMainScreen()
        },1500)
    }

    private fun launchMainScreen() {
        startActivity(Intent(this,
            CompressorBaseActivity::class.java))
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler?.removeCallbacksAndMessages(null)
    }
}