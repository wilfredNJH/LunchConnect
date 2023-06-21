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
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.NoteData
import kotlinx.android.synthetic.main.activity_profile.*
import java.io.FileNotFoundException
import java.util.*

class ProfileActivity : AppCompatActivity() {

    private var isEdit: Boolean = false
    private val PICK_IMAGE = 1

    // image information
    private var profileImagePath : String? = null
    private var profileImage : Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLayout()

        val buttonLogout: Button = findViewById(R.id.buttonLogout)
        buttonLogout.setOnClickListener {
            Backend.signOut()
            finishAffinity()
        }
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


        if(isEdit){
            loadData()
        }else{
            loadDataInit()
        }


        // Set click listener for switch button
        findViewById<Button>(R.id.switch_button).setOnClickListener {
            if (isEdit) {
                // save data
                Log.d("editt","entered")
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

        val innerName = findViewById<EditText>(R.id.nameProfileET).text.toString()
        val innerDepartment = findViewById<EditText>(R.id.departmentProfileET).text.toString()
        val innerJobRole = findViewById<EditText>(R.id.short_descriptionET).text.toString()
        val innerDes = findViewById<EditText>(R.id.short_descriptionET).text.toString()
        val innerHobbies = findViewById<EditText>(R.id.hobbies_interestET).text.toString()
        val innerLocation = findViewById<EditText>(R.id.locationET).text.toString()

        // inside the addNote.setOnClickListener() method and after the Note() object is created.
//        if (this.profileImagePath != null) {
//            note.imageName = UUID.randomUUID().toString()
//            //note.setImage(this.noteImage)
//            note.image = this.profileImage
//
//            // asynchronously store the image (and assume it will work)
//            Backend.storeImage(this.profileImagePath!!, note.imageName!!)
//        }

        // query
        Amplify.API.query(
            ModelQuery.list(NoteData::class.java),
            { response ->
                Log.i("edit note", "Queried")

                for (noteData in response.data) {
                    val newNoteData = noteData.copyOfBuilder()
                        .name(innerName)
                        .department(innerDepartment)
                        .jobRole(innerJobRole)
                        .description(innerDes)
                        .hobbies(innerHobbies)
                        .location(innerLocation)
                        .build()

                    Amplify.API.mutate(
                        ModelMutation.update(newNoteData),
                        { response ->
                            Log.i("mutation", "Updating")
                            if (response.hasErrors()) {
                                Log.e("mutation", response.errors.first().message)
                            } else {
                                Log.i("mutation", "Updating Note with id: " + response.data.id)
                            }

                            findViewById<TextView>(R.id.nameProfile).setText(innerName)
                            findViewById<TextView>(R.id.departmentProfile).setText(innerDepartment)
                            findViewById<TextView>(R.id.job_role).setText(innerJobRole)
                            findViewById<TextView>(R.id.short_description).setText(innerDes)
                            findViewById<TextView>(R.id.hobbies_interest).setText(innerHobbies)
                            findViewById<TextView>(R.id.location).setText(innerLocation)
                        },
                        { error -> Log.e("mutation", "Updating failed", error) }
                    )

                    // add it to UserData, this will trigger a UI refresh
                    UserData.editNote(UserData.Note.from(newNoteData))
                }
            },
            { error -> Log.e("edit note ", "Query failure", error) }
        )

    }
    private fun loadData() {

        findViewById<EditText>(R.id.nameProfileET).setText(UserData.getName())
        findViewById<EditText>(R.id.departmentProfileET).setText(UserData.getDepartment())
        findViewById<EditText>(R.id.job_roleET).setText(UserData.getJobRole())
        findViewById<EditText>(R.id.short_descriptionET).setText(UserData.getDescription())
        findViewById<EditText>(R.id.hobbies_interestET).setText(UserData.getHobbies())
        findViewById<EditText>(R.id.locationET).setText(UserData.getLocation())
    }

    private fun loadDataInit() {

        findViewById<TextView>(R.id.nameProfile).setText(UserData.getName())
        findViewById<TextView>(R.id.departmentProfile).setText(UserData.getDepartment())
        findViewById<TextView>(R.id.job_role).setText(UserData.getJobRole())
        findViewById<TextView>(R.id.short_description).setText(UserData.getDescription())
        findViewById<TextView>(R.id.hobbies_interest).setText(UserData.getHobbies())
        findViewById<TextView>(R.id.location).setText(UserData.getLocation())
    }




}
