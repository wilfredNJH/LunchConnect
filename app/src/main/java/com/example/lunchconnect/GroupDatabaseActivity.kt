package com.example.lunchconnect

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.content_main.*

class GroupDatabaseActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "GroupDatabaseActivity"
    }

    private lateinit var fabAdd: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_database)

        // initialize the fabAdd button
        fabAdd = findViewById(R.id.fabAdd)

        Log.d(TAG, "Showing fabADD")
        // register a click listener
        fabAdd.setOnClickListener {
            // to add note
            startActivity(Intent(this, AddNoteActivity::class.java))
        }
        // prepare our List view and RecyclerView (cells)
        setupRecyclerView(item_list)
    }

    // recycler view is the list of cells
    private fun setupRecyclerView(recyclerView: RecyclerView) {

        // add a touch gesture handler to manager the swipe to delete gesture
        val itemTouchHelper = ItemTouchHelper(SwipeCallback(this))
        itemTouchHelper.attachToRecyclerView(recyclerView)

        // update individual cell when the Note data are modified
        UserGroupData.groups().observe(this, Observer<MutableList<UserGroupData.GroupNote>> { groups ->
            Log.d(TAG, "Note observer received ${groups.size} notes")

            // let's create a RecyclerViewAdapter that manages the individual cells
            recyclerView.adapter = GroupRecyclerViewAdapter(groups)
        })
    }

    // receive the web redirect after authentication
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Backend.handleWebUISignInResponse(requestCode, resultCode, data)
    }
}