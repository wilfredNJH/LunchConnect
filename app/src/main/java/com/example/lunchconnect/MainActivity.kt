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
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var itemList: RecyclerView
    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        itemList = findViewById(R.id.item_list)

        setSupportActionBar(toolbar)
        setupRecyclerView(itemList)

        // Assuming you have a button or any other trigger to start MapsActivity
        button = findViewById(R.id.buttonGoogleMap)
        button.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        UserData.notes().observe(this, Observer<MutableList<UserData.Note>> { notes ->
            Log.d(TAG, "Note observer received ${notes.size} notes")
            recyclerView.adapter = NoteRecyclerViewAdapter(notes)
        })
    }

    companion object {
        private const val TAG = "MainActivity"
    }

}