package com.example.lunchconnect

// We initialize our singleton Backend object when application launches.

import android.app.Application

class AndroidGettingStartedApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // initialize Amplify when application is starting
        Backend.initialize(applicationContext)

        // Create an instance of GMapApplication and call its onCreate() method
//        val gMapApplication = GMapApplication()
//        gMapApplication.onCreate()
    }
}