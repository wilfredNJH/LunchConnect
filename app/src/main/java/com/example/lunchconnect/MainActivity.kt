package com.example.lunchconnect

/*
the main layout is a RecyclerView that manages the list of individual cells we created previously
the main activity class observes changes on the list of Notes and creates an NoteRecyclerViewAdapter to create individual cells.

 */
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import java.io.FileNotFoundException

class MainActivity : AppCompatActivity() {

    private lateinit var friends: Button
    private lateinit var group: Button
    private lateinit var questionnaire: Button
    private lateinit var groupdatabase: Button
    private lateinit var profileimage: ImageView

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        group =findViewById(R.id.buttonGroup)
        questionnaire = findViewById(R.id.button_questionnaire)
        friends = findViewById(R.id.button_friends)
        profileimage = findViewById(R.id.profile_main)
        groupdatabase = findViewById(R.id.buttonGroupDatabase)

        // Load the image from internal storage
        loadImage("profile_image.png")

        questionnaire.setOnClickListener {
            val intent = Intent(this, PointsAndBadges::class.java)
            startActivity(intent)
        }

        friends.setOnClickListener {
            val intent = Intent(this, FriendActivity::class.java)
            startActivity(intent)
        }

        group.setOnClickListener {
            val intent = Intent(this, GroupData::class.java)
            startActivity(intent)
        }

        profileimage.setOnClickListener {
            Log.d(TAG, "ImageView was clicked")
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        groupdatabase.setOnClickListener {
            val intent = Intent(this, GroupDatabaseActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        loadImage("profile_image.png")
    }

    private fun loadImageFromInternalStorage(filename: String): Bitmap? {
        val fis = openFileInput(filename)
        val bitmap = BitmapFactory.decodeStream(fis)
        fis.close()
        return bitmap
    }

    private fun loadImage(filename: String)
    {
        // Load the image from internal storage
        try {
            val bitmap = loadImageFromInternalStorage(filename)
            if (bitmap != null) {
                findViewById<ImageView>(R.id.profile_main).setImageBitmap(bitmap)
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            // Handle the situation when the file does not exist, perhaps by showing a default image or a toast message
        }
    }
}