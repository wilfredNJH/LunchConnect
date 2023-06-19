package com.example.lunchconnect

// We initialize our singleton Backend object when application launches.

import android.app.Application

class AndroidGettingStartedApplication : GMapApplication() {

    override fun onCreate() {
        super.onCreate()

        // initialize Amplify when application is starting
        Backend.initialize(applicationContext)
    }
}