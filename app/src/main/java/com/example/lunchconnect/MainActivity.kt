package com.example.lunchconnect

/*
the main layout is a RecyclerView that manages the list of individual cells we created previously
the main activity class observes changes on the list of Notes and creates an NoteRecyclerViewAdapter to create individual cells.

 */
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.ButtonBarLayout
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var profile: Button
    private lateinit var questionnaire: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        // prepare our List view and RecyclerView (cells)
        setupRecyclerView(item_list)

        // register an observer on Userdata.isSignedIn value. The closure is called when isSignedIn value changes. Right now, we just change the lock icon :
        // open when the user is authenticated and closed when the user has no session.
        setupAuthButton(UserData)

        profile = findViewById(R.id.button_profile)
        questionnaire = findViewById(R.id.button_questionnaire)


        UserData.isSignedIn.observe(this, Observer<Boolean> { isSignedUp ->
            // update UI
            Log.i(TAG, "isSignedIn changed : $isSignedUp")

            //animation inspired by https://www.11zon.com/zon/android/multiple-floating-action-button-android.php
            if (isSignedUp) {
                fabAuth.setImageResource(R.drawable.ic_baseline_lock_open)
                Log.d(TAG, "Showing fabADD")
                fabAdd.show()
                fabAdd.animate().translationY(0.0F - 1.1F * fabAuth.customSize)
            } else {
                fabAuth.setImageResource(R.drawable.ic_baseline_lock)
                Log.d(TAG, "Hiding fabADD")
                fabAdd.hide()
                fabAdd.animate().translationY(0.0F)
            }
        })


        questionnaire.setOnClickListener {
            val intent = Intent(this, Questionnaire::class.java)
            startActivity(intent)
        }

        profile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        // register a click listener
        fabAdd.setOnClickListener {
            startActivity(Intent(this, AddNoteActivity::class.java))
        }
    }

    // recycler view is the list of cells
    private fun setupRecyclerView(recyclerView: RecyclerView) {

        // add a touch gesture handler to manager the swipe to delete gesture
        val itemTouchHelper = ItemTouchHelper(SwipeCallback(this))
        itemTouchHelper.attachToRecyclerView(recyclerView)

        // update individual cell when the Note data are modified
        UserData.notes().observe(this, Observer<MutableList<UserData.Note>> { notes ->
            Log.d(TAG, "Note observer received ${notes.size} notes")

            // let's create a RecyclerViewAdapter that manages the individual cells
            recyclerView.adapter = NoteRecyclerViewAdapter(notes)
        })
    }

    // anywhere in the MainActivity class
    private fun setupAuthButton(userData: UserData) {

        // register a click listener
        fabAuth.setOnClickListener { view ->

            val authButton = view as FloatingActionButton

            if (userData.isSignedIn.value!!) {
                authButton.setImageResource(R.drawable.ic_baseline_lock_open)
                Backend.signOut()
            } else {
                authButton.setImageResource(R.drawable.ic_baseline_lock_open)
                Backend.signIn(this)
            }
        }
    }

    // receive the web redirect after authentication
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Backend.handleWebUISignInResponse(requestCode, resultCode, data)
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}