package com.example.lunchconnect

/*
The UserData class is responsible to hold user data, namely a isSignedIn flag to track current authentication status and a list of Note objects.
These two properties are implemented according to the LiveData publish / subscribe framework. It allows the Graphical User Interface (GUI) to subscribe to changes and to react accordingly. To learn more about LiveData, you can read this doc or follow this short video tutorial. To follow best practice, keep the MutableLiveData property private and only expose the readonly LiveData property. Some additional boilerplate code is required when the data to publish is a list to make sure observers are notified when individual components in the list are modified.
We also added a Note data class, just to hold the data of individual notes. Two distinct properties are used for ImageName and Image. Image will be taken care of in a subsequent module.
Implemented the singleton design pattern for the UserData object as it allows referal to it from anywhere in the application just with UserData.
 */

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.amplifyframework.datastore.generated.model.NoteData

// a singleton to hold user data (this is a ViewModel pattern, without inheriting from ViewModel)
object UserData {

    private const val TAG = "UserData"

    //
    // observable properties
    //

    // signed in status
    private val _isSignedIn = MutableLiveData<Boolean>(false)
    var isSignedIn: LiveData<Boolean> = _isSignedIn

    fun setSignedIn(newValue : Boolean) {
        // use postvalue() to make the assignation on the main (UI) thread
        _isSignedIn.postValue(newValue)
    }

    // the notes
    private val _notes = MutableLiveData<MutableList<Note>>(mutableListOf())

    // please check https://stackoverflow.com/questions/47941537/notify-observer-when-item-is-added-to-list-of-livedata
    private fun <T> MutableLiveData<T>.notifyObserver() {
        this.postValue(this.value)
    }
    fun notifyObserver() {
        this._notes.notifyObserver()
    }

    fun notes() : LiveData<MutableList<Note>>  = _notes
    fun addNote(n : Note) {
        val notes = _notes.value
        if (notes != null) {
            notes.add(n)
            _notes.notifyObserver()
        } else {
            Log.e(TAG, "addNote : note collection is null !!")
        }
    }
    fun editNote(n : Note) {
        val notes = _notes.value
        if (notes != null) {
            notes[0] = n // replace the first note with the new note
            _notes.notifyObserver()
        } else {
            Log.e(TAG, "addNote : note collection is null !!")
        }
    }
    fun deleteNote(at: Int) : Note?  {
        val note = _notes.value?.removeAt(at)
        _notes.notifyObserver()
        return note
    }

    fun resetNotes() {
        this._notes.value?.clear()  //used when signing out
        _notes.notifyObserver()
    }

    fun getPoints(): Int {
        return _notes.value?.get(0)?.points?:0
    }

    fun getName(): String {
        return _notes.value?.get(0)?.name?:""
    }

    fun getDepartment(): String {
        return _notes.value?.get(0)?.department?:""
    }

    fun getJobRole(): String {
        return _notes.value?.get(0)?.jobRole?:""
    }

    fun getDescription(): String {
        return _notes.value?.get(0)?.description?:""
    }

    fun getHobbies(): String {
        return _notes.value?.get(0)?.hobbies?:""
    }

    fun getLocation(): String {
        return _notes.value?.get(0)?.location?:""
    }


    // a note data class
    data class Note(val id: String, val name: String, val description: String, var imageName: String? = null) {
    fun getPoints(): Int {
        return _notes.value?.get(0)?.points?:0
    }

    fun setPoints(newPoints: Int) {
        val notes = _notes.value
        if (notes != null && notes.isNotEmpty()) {
            notes[0].points = newPoints
            _notes.notifyObserver()
        } else {
            Log.e(TAG, "setPoints: note collection is null or empty!")
        }
    }


    fun getName(): String {
        return _notes.value?.get(0)?.name?:""
    }

    fun getDepartment(): String {
        return _notes.value?.get(0)?.department?:""
    }

    fun getJobRole(): String {
        return _notes.value?.get(0)?.jobRole?:""
    }

    fun getDescription(): String {
        return _notes.value?.get(0)?.description?:""
    }

    fun getHobbies(): String {
        return _notes.value?.get(0)?.hobbies?:""
    }

    fun getLocation(): String {
        return _notes.value?.get(0)?.location?:""
    }


    // a note data class
    data class Note(val id: String, val name: String,val department: String,val jobRole : String, val description: String,
                    val hobbies: String, val location: String, var points: Int, val badges: Int,var imageName: String? = null) {
        override fun toString(): String = name


        // return an API NoteData from this Note object
        val data : NoteData
            get() = NoteData.builder()
                .name(this.name)
                .department(this.department)
                .jobRole(this.jobRole)
                .description(this.description)
                .hobbies(this.hobbies)
                .location(this.location)
                .points(this.points)
                .badges(this.badges)
                .image(this.imageName)
                .id(this.id)
                .build()

        /*
        To load images, we modify the static from method on the Note data class. That way, every time a NoteData object returned
        by the API is converted to a Note object, the image is loaded in parallel. When the image is loaded,
        we notify the LiveData's UserData to let observers know about the change. This triggers a UI refresh.
         */
        // static function to create a Note from a NoteData API object
        companion object {
            fun from(noteData : NoteData) : Note {
                val result = Note(noteData.id, noteData.name,noteData.department,noteData.jobRole, noteData.description,
                    noteData.hobbies,noteData.location,noteData.points,noteData.badges,noteData.image)

                if (noteData.image != null) {
                    Backend.retrieveImage(noteData.image!!) {
                        result.image = it

                        // force a UI update
                        with(UserData) { notifyObserver() }
                    }
                }
                return result
            }
        }

        // bitmap image
        var image : Bitmap? = null

    }
}