package com.example.lunchconnect

/*
the main layout is a RecyclerView that manages the list of individual cells we created previously
the main activity class observes changes on the list of Notes and creates an NoteRecyclerViewAdapter to create individual cells.

 */
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        // prepare our List view and RecyclerView (cells)
        setupRecyclerView(item_list)
    }

    // recycler view is the list of cells
    private fun setupRecyclerView(recyclerView: RecyclerView) {

        // update individual cell when the Note data are modified
        UserData.notes().observe(this, Observer<MutableList<UserData.Note>> { notes ->
            Log.d(TAG, "Note observer received ${notes.size} notes")

            // let's create a RecyclerViewAdapter that manages the individual cells
            recyclerView.adapter = NoteRecyclerViewAdapter(notes)
        })
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}