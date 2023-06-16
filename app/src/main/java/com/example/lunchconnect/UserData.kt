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
    fun deleteNote(at: Int) : Note?  {
        val note = _notes.value?.removeAt(at)
        _notes.notifyObserver()
        return note
    }

    fun resetNotes() {
        this._notes.value?.clear()  //used when signing out
        _notes.notifyObserver()
    }


    // a note data class
    data class Note(val id: String, val name: String, val description: String, var imageName: String? = null) {
        override fun toString(): String = name

        // bitmap image
        var image : Bitmap? = null

    }
}