package com.example.lunchconnect

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.amplifyframework.datastore.generated.model.Group
import kotlin.math.log

// a singleton to hold user data (this is a ViewModel pattern, without inheriting from ViewModel)
object UserGroupData {

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
    private val _groups = MutableLiveData<MutableList<GroupNote>>(mutableListOf())

    // please check https://stackoverflow.com/questions/47941537/notify-observer-when-item-is-added-to-list-of-livedata
    private fun <T> MutableLiveData<T>.notifyObserver() {
        this.postValue(this.value)
    }
    fun notifyObserver() {
        this._groups.notifyObserver()
    }

    fun groups() : LiveData<MutableList<GroupNote>>  = _groups
    fun addGroup(n : GroupNote) {
        val groups = _groups.value
        if (groups != null) {
            groups.add(n)
            _groups.notifyObserver()
        } else {
            Log.e(TAG, "addNote : note collection is null !!")
        }
    }
    fun editGroup(n : GroupNote) {
        val groups = _groups.value
        if (groups != null) {
            groups[0] = n // replace the first note with the new note
            _groups.notifyObserver()
        } else {
            Log.e(TAG, "addNote : note collection is null !!")
        }
    }
    fun deleteGroup(at: Int) : GroupNote?  {
        val groups = _groups.value?.removeAt(at)
        _groups.notifyObserver()
        return groups
    }

    fun resetGroup() {
        this._groups.value?.clear()  //used when signing out
        _groups.notifyObserver()
    }


    // a note data class
    data class GroupNote(val id: String, val members: MutableList<String> , val location : String, val time : String,
    val specialRequest : String) {
        override fun toString(): String = location


        // return an API NoteData from this Note object
        val data : Group
            get() = Group.builder()
                .members(this.members)
                .location(this.location)
                .time(this.time)
                .specialRequest((this.specialRequest))
                .id(this.id)
                .build()

        /*
        To load images, we modify the static from method on the Note data class. That way, every time a NoteData object returned
        by the API is converted to a Note object, the image is loaded in parallel. When the image is loaded,
        we notify the LiveData's UserData to let observers know about the change. This triggers a UI refresh.
         */
        // static function to create a Note from a NoteData API object
        companion object {
            fun from(GroupnoteData : Group) : GroupNote {
                val result = GroupNote(GroupnoteData.id, GroupnoteData.members,GroupnoteData.location,
                    GroupnoteData.time,GroupnoteData.specialRequest)

                return result
            }
        }
    }
}