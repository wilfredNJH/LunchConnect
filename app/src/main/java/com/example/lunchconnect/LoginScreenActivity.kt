package com.example.lunchconnect

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_login_screen.*


class LoginScreenActivity : AppCompatActivity() {

    // defines a private constant TAG with the value "LoginScreenActivity"
    companion object {
        private const val TAG = "LoginScreenActivity"
    }

    private var isSignedOutOnDestroy = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)


        // register an observer on Userdata.isSignedIn value. The closure is called when isSignedIn value changes.
        // Right now, we just change the lock icon :
        // open when the user is authenticated and closed when the user has no session.
        setupAuthButton(UserData)

        UserData.isSignedIn.observe(this, Observer<Boolean> { isSignedUp ->
            // update UI
            Log.i(TAG, "isSignedIn changed : $isSignedUp")

            //animation inspired by https://www.11zon.com/zon/android/multiple-floating-action-button-android.php
            if (isSignedUp) {
//                fabAuth.setImageResource(R.drawable.ic_baseline_lock_open)
//                Log.d(TAG, "Showing fabADD")
//                fabAdd.show()
//                fabAdd.animate().translationY(0.0F - 1.1F * fabAuth.customSize)

                // transition to main
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

            } else {
                fabAuth.setImageResource(R.drawable.ic_baseline_lock)
                Log.d(TAG, "Hiding fabADD")
                fabAdd.hide()
                fabAdd.animate().translationY(0.0F)
            }
        })

        // register a click listener
        fabAdd.setOnClickListener {
            startActivity(Intent(this, AddNoteActivity::class.java))
        }
    }

    // anywhere in the MainActivity class
    private fun setupAuthButton(userData: UserData) {

        // register a click listener
        fabAuth.setOnClickListener { view ->

            val authButton = view as FloatingActionButton

            if (userData.isSignedIn.value!!) {
                authButton.setImageResource(R.drawable.ic_baseline_lock_open)
                signOutAndFinish()
            } else {
                authButton.setImageResource(R.drawable.ic_baseline_lock_open)
                Backend.signIn(this)
            }
        }
    }

    // Checks if the activity is being destroyed abruptly without signing out.
    // If not signed out, performs the sign-out action.
    override fun onDestroy() {
        super.onDestroy()

//        if (UserData.isSignedIn.value == true && !isSignedOutOnDestroy) {
            signOut()
//        }
    }


    // Signs out the user and finishes the activity
    private fun signOutAndFinish() {
        signOut()
        isSignedOutOnDestroy = true
        finish()
    }

    // Performs the sign-out action by calling the appropriate method in the Backend class.
    // Modify this method to implement the actual sign-out logic specific to the application.
    private fun signOut() {
        // Perform the sign-out action here
        Backend.signOut()
    }
}