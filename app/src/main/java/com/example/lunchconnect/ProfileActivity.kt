package com.example.lunchconnect

import android.os.Bundle
import android.content.Intent
import android.provider.MediaStore
import android.graphics.Bitmap
import android.app.Activity
import android.content.Context
import kotlinx.coroutines.*
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.NoteData
import com.google.android.material.shape.CornerFamily
import kotlinx.android.synthetic.main.activity_add_note.*
import kotlinx.android.synthetic.main.activity_profile.*
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.InputStream
import java.util.*

class ProfileActivity : AppCompatActivity() {
    // Declare a coroutine scope as a class member
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    private var isEdit: Boolean = false
    private val PICK_IMAGE = 1

    // image information
    private var profileImagePath : String? = null
    private var profileImage : Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLayout()

        // create rounded corners for the image
        profile_image.shapeAppearanceModel = profile_image.shapeAppearanceModel
            .toBuilder()
            .setAllCorners(CornerFamily.ROUNDED, 150.0f)
            .build()

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
                val i = Intent(
                    Intent.ACTION_GET_CONTENT,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                startActivityForResult(i, ProfileActivity.SELECT_PHOTO)
            }
        } else {
            setContentView(R.layout.activity_profile)
        }


        if(isEdit){
            loadData()
        }else{
            loadDataInit()
        }

        // Edit Button
//        val imageViewButtonEdit = findViewById<ImageView>(R.id.imageView_button_edit)
//        imageViewButtonEdit.setOnClickListener {
//            Toast.makeText(this, "Edit Button clicked!", Toast.LENGTH_SHORT).show()
//            if (isEdit) {
//                // save data
//                Log.d("editt","entered")
//                saveData()
//            }
//            isEdit = !isEdit
//            setLayout()
//        }



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


    private fun saveData() {

        val innerName = findViewById<EditText>(R.id.nameProfileET).text.toString()
        val innerDepartment = findViewById<EditText>(R.id.departmentProfileET).text.toString()
        val innerJobRole = findViewById<EditText>(R.id.short_descriptionET).text.toString()
        val innerDes = findViewById<EditText>(R.id.short_descriptionET).text.toString()
        val innerHobbies = findViewById<EditText>(R.id.hobbies_interestET).text.toString()
        val innerLocation = findViewById<EditText>(R.id.locationET).text.toString()



        // query
        Amplify.API.query(
            ModelQuery.list(NoteData::class.java),
            { response ->
                Log.i("edit note", "Queried")

                for (noteData in response.data) {

                    // getting the user data from the note
                    val userNoteData = UserData.Note.from(noteData)

                    // inside the addNote.setOnClickListener() method and after the Note() object is created.
                    if (this.profileImagePath != null) {
                        userNoteData.imageName = UUID.randomUUID().toString()
                        //note.setImage(this.noteImage)
                        userNoteData.image = this.profileImage

                        // asynchronously store the image (and assume it will work)
                        Backend.storeImage(this.profileImagePath!!, userNoteData.imageName!!)
                    }

                    val newNoteData = noteData.copyOfBuilder()
                        .name(innerName)
                        .department(innerDepartment)
                        .jobRole(innerJobRole)
                        .description(innerDes)
                        .hobbies(innerHobbies)
                        .location(innerLocation)
                        .image(userNoteData.imageName)
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

        loadImageData()
    }

    private fun loadImageData(){

        // query
        Amplify.API.query(
            ModelQuery.list(NoteData::class.java),
            { response ->
                Log.i("load image", "Queried")

                for (noteData in response.data) {
                    // getting the user data from the note
                    val userNoteData = UserData.Note.from(noteData)

                    coroutineScope.launch {
                        delay(1000) // Delay in milliseconds (2 seconds in this example)

                        // Code to be executed after the delay
                        // Write your delayed code logic here


                        Log.d("setting image","image set load image data ")
                        Log.i("download", "Successfully downloaded2:")
                        if (userNoteData.image != null) {
                            Log.i("download", "Successfully downloaded3:")
                            Log.i("setting image", "Image loaded")
                            profile_image.setImageBitmap(userNoteData.image)
                        }
                    }

//                    Log.d("setting image","image set load image data ")
//                    Log.i("download", "Successfully downloaded2:")
//                    if (userNoteData.image != null) {
//                        Log.i("download", "Successfully downloaded3:")
//                        Log.i("setting image", "Image loaded")
//                        profile_image.setImageBitmap(userNoteData.image)
//                    }
                }
            },
            { error -> Log.e("load image ", "Query failure", error) }
        )

    }



    private fun loadDataInit() {

        findViewById<TextView>(R.id.nameProfile).setText(UserData.getName())
        findViewById<TextView>(R.id.departmentProfile).setText(UserData.getDepartment())
        findViewById<TextView>(R.id.job_role).setText(UserData.getJobRole())
        findViewById<TextView>(R.id.short_description).setText(UserData.getDescription())
        findViewById<TextView>(R.id.hobbies_interest).setText(UserData.getHobbies())
        findViewById<TextView>(R.id.location).setText(UserData.getLocation())

        loadImageData()
    }


    /*
 code consumes the selected image as an InputStream, twice. The first InputStream creates a Bitmap image to display in the UI, the second InputStream saves a temporary file to send to the backend.

 This module goes through a temporary file because the Amplify API consumes Fileobjects. While not the most efficient design, the code is simple
  */
    override fun onActivityResult(requestCode: Int, resultCode: Int, imageReturnedIntent: Intent?) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent)
        Log.d(TAG, "Select photo activity result : $imageReturnedIntent")
        when (requestCode) {
            ProfileActivity.SELECT_PHOTO -> if (resultCode == RESULT_OK) {
                val selectedImageUri : Uri? = imageReturnedIntent!!.data

                // read the stream to fill in the preview
                var imageStream: InputStream? = contentResolver.openInputStream(selectedImageUri!!)
                val selectedImage = BitmapFactory.decodeStream(imageStream)
                val ivPreview: ImageView = findViewById<View>(R.id.profile_image) as ImageView
                ivPreview.setImageBitmap(selectedImage)

                // store the image to not recreate the Bitmap every time
                this.profileImage = selectedImage

                // read the stream to store to a file
                imageStream = contentResolver.openInputStream(selectedImageUri)
                val tempFile = File.createTempFile("image", ".image")
                copyStreamToFile(imageStream!!, tempFile)

                // store the path to create a note
                this.profileImagePath = tempFile.absolutePath

                Log.d(TAG, "Selected image : ${tempFile.absolutePath}")
            }
        }
    }

    private fun copyStreamToFile(inputStream: InputStream, outputFile: File) {
        inputStream.use { input ->
            val outputStream = FileOutputStream(outputFile)
            outputStream.use { output ->
                val buffer = ByteArray(4 * 1024) // buffer size
                while (true) {
                    val byteCount = input.read(buffer)
                    if (byteCount < 0) break
                    output.write(buffer, 0, byteCount)
                }
                output.flush()
                output.close()
            }
        }
    }



    companion object {
        private const val TAG = "ProfileActivity"
        // add this to the companion object
        private const val SELECT_PHOTO = 100
    }

}
