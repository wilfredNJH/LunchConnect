package com.example.lunchconnect

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.content.Intent
import android.provider.MediaStore
import android.graphics.Bitmap
import android.app.Activity
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {

    private var isEdit: Boolean = false
    private val PICK_IMAGE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLayout()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun setLayout() {
        if (isEdit) {
            setContentView(R.layout.activity_profile_edit)

            // Set click listener for change image button
            findViewById<Button>(R.id.change_image_button).setOnClickListener {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, PICK_IMAGE)
            }
        } else {
            setContentView(R.layout.activity_profile)
        }

        // Set click listener for switch button
        findViewById<Button>(R.id.switch_button).setOnClickListener {
            if (isEdit) {
                // save data
            }
            isEdit = !isEdit
            setLayout()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(isEdit) {
            if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
                val selectedImage = data.data
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImage)
                findViewById<ImageView>(R.id.profile_image).setImageBitmap(bitmap)
            }
        }
    }
}
