package com.example.lunchconnect

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.content.Intent
import android.provider.MediaStore
import android.graphics.Bitmap
import android.app.Activity
import android.content.Context
import android.graphics.BitmapFactory
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.FileNotFoundException

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


        // Load the image from internal storage
        try {
            val bitmap = loadImageFromInternalStorage("profile_image.png")
            if (bitmap != null) {
                findViewById<ImageView>(R.id.profile_image).setImageBitmap(bitmap)
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            // Handle the situation when the file does not exist, perhaps by showing a default image or a toast message
        }


        loadData()

        // Set click listener for switch button
        findViewById<Button>(R.id.switch_button).setOnClickListener {
            if (isEdit) {
                // save data
                saveData()
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

                // Save the bitmap to internal storage
                saveImageToInternalStorage(bitmap, "profile_image.png")

            }
        }
    }


    private fun saveImageToInternalStorage(bitmap: Bitmap, filename: String) {
        val fos = openFileOutput(filename, Context.MODE_PRIVATE)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
        fos.close()
    }
    private fun loadImageFromInternalStorage(filename: String): Bitmap? {
        val fis = openFileInput(filename)
        val bitmap = BitmapFactory.decodeStream(fis)
        fis.close()
        return bitmap
    }

    private fun saveData() {
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("name", findViewById<EditText>(R.id.name).text.toString())
        editor.putString("department", findViewById<EditText>(R.id.department).text.toString())
        editor.putString("job_role", findViewById<EditText>(R.id.job_role).text.toString())
        editor.putString("short_description", findViewById<EditText>(R.id.short_description).text.toString())
        editor.putString("hobbies_interest", findViewById<EditText>(R.id.hobbies_interest).text.toString())
        editor.putString("location", findViewById<EditText>(R.id.location).text.toString())

        editor.apply()
    }
    private fun loadData() {
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)

        findViewById<TextView>(R.id.name).setText(sharedPreferences.getString("name", ""))
        findViewById<TextView>(R.id.department).setText(sharedPreferences.getString("department", ""))
        findViewById<TextView>(R.id.job_role).setText(sharedPreferences.getString("job_role", ""))
        findViewById<TextView>(R.id.short_description).setText(sharedPreferences.getString("short_description", ""))
        findViewById<TextView>(R.id.hobbies_interest).setText(sharedPreferences.getString("hobbies_interest", ""))
        findViewById<TextView>(R.id.location).setText(sharedPreferences.getString("location", ""))
    }




}
