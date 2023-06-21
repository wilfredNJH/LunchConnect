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
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.NoteData
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.FileNotFoundException

class MainActivity : AppCompatActivity() {

    // Declare a coroutine scope as a class member
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

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
            // query
            Amplify.API.query(
                ModelQuery.list(NoteData::class.java),
                { response ->
                    for (noteData in response.data) {
                        // getting the user data from the note
                        val userNoteData = UserData.Note.from(noteData)

                        coroutineScope.launch {
                            delay(1000) // Delay in milliseconds (2 seconds in this example)

                            // Code to be executed after the delay
                            if (userNoteData.image != null) {
                                profile_main.setImageBitmap(userNoteData.image)
                            }
                        }
                    }
                },
                { error -> Log.e("load image ", "Query failure", error) }
            )

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            // Handle the situation when the file does not exist, perhaps by showing a default image or a toast message
        }
    }
}