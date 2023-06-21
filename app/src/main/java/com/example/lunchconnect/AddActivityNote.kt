package com.example.lunchconnect

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.shape.CornerFamily
import kotlinx.android.synthetic.main.activity_add_note.*
import kotlinx.android.synthetic.main.activity_profile.*
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.*

class AddNoteActivity : AppCompatActivity()  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        cancel.setOnClickListener {
            this.finish()
        }

        // add note button
        addGroup.setOnClickListener {
            val innerLocation = findViewById<EditText>(R.id.groupLocationET).text.toString()
            val innerTime = findViewById<EditText>(R.id.groupTimeET).text.toString()
            val innerSpecialRequest = findViewById<EditText>(R.id.groupSpecialRequestET).text.toString()

            // create a group object
            val group = UserGroupData.GroupNote(
                UUID.randomUUID().toString(),
                MutableList<String>(1) { "s" },
                innerLocation,
                innerTime,
                innerSpecialRequest,
            )

            // store it in the backend
            Backend.createGroup(group)

            // add it to UserData, this will trigger a UI refresh
            UserGroupData.addGroup(group)

            // close activity
            this.finish()
        }
    }

    companion object {
        private const val TAG = "AddGroupActivity"
    }
}