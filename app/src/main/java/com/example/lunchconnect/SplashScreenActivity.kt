package com.example.lunchconnect

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class SplashScreenActivity : AppCompatActivity() {
    private val SPLASH_DELAY: Long = 3000 // Delay in milliseconds (3 seconds)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val handler = Handler()
        handler.postDelayed({
            val intent = Intent(this@SplashScreenActivity, LoginScreenActivity::class.java)
            overridePendingTransition(R.anim.slide_out_left, android.R.anim.fade_out)
            finish()
            startActivity(intent)
        }, SPLASH_DELAY)
    }
}
