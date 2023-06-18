package com.example.lunchconnect

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_note.*
import java.util.*

class AddNoteActivity : AppCompatActivity()  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        cancel.setOnClickListener {
            this.finish()
        }

        addNote.setOnClickListener {

            // create a note object
            val note = UserData.Note(
                UUID.randomUUID().toString(),
                name?.text.toString(),
                description?.text.toString()
            )

            // store it in the backend
            Backend.createNote(note)

            // add it to UserData, this will trigger a UI refresh
            UserData.addNote(note)

            // close activity
            this.finish()
        }
    }

    companion object {
        private const val TAG = "AddNoteActivity"
    }
}