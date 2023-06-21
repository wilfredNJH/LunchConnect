package com.example.lunchconnect

/*
 a Backend class to group the code to interact with our backend. I use a singleton design pattern to make it easily available through the application and to ensure the Amplify libraries are initialized only once.
The class initializer takes care of initializing the Amplify libraries.
 */

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.amplifyframework.AmplifyException
import com.amplifyframework.api.aws.AWSApiPlugin
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.auth.AuthChannelEventName
import com.amplifyframework.auth.AuthException
import com.amplifyframework.auth.AuthUser
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession
import com.amplifyframework.auth.cognito.result.AWSCognitoAuthSignOutResult
import com.amplifyframework.auth.result.AuthSessionResult
import com.amplifyframework.auth.result.AuthSignInResult
import com.amplifyframework.core.Amplify
import com.amplifyframework.core.InitializationStatus
import com.amplifyframework.datastore.generated.model.NoteData
import com.amplifyframework.hub.HubChannel
import com.amplifyframework.hub.HubEvent
import com.amplifyframework.storage.StorageAccessLevel
import com.amplifyframework.storage.options.StorageDownloadFileOptions
import com.amplifyframework.storage.options.StorageRemoveOptions
import com.amplifyframework.storage.options.StorageUploadFileOptions
import com.amplifyframework.storage.s3.AWSS3StoragePlugin
import kotlinx.android.synthetic.main.activity_profile.*
import java.io.File
import java.io.FileInputStream
import java.util.*

object Backend {

    private const val TAG = "Backend"

    // inside Backend class
    //To track the changes of authentication status, we add code to subscribe to Authentication events sent by Amplify.
    // We initialize the Hub in the Backend.initialize() method.
    fun initialize(applicationContext: Context): Backend {
        try {
            Amplify.addPlugin(AWSCognitoAuthPlugin())
            Amplify.addPlugin(AWSApiPlugin())
            Amplify.addPlugin(AWSS3StoragePlugin())

            Amplify.configure(applicationContext)

            Log.i(TAG, "Initialized Amplify")
        } catch (e: AmplifyException) {
            Log.e(TAG, "Could not initialize Amplify", e)
        }

        // in Backend.initialize() function, after the try/catch block but before the return statement

        Log.i(TAG, "registering hub event")

        // listen to auth event
        Amplify.Hub.subscribe(HubChannel.AUTH) { hubEvent: HubEvent<*> ->

            when (hubEvent.name) {
                InitializationStatus.SUCCEEDED.toString() -> {
                    Log.i(TAG, "Amplify successfully initialized")
                }
                InitializationStatus.FAILED.toString() -> {
                    Log.i(TAG, "Amplify initialization failed")
                }
                else -> {
                    when (AuthChannelEventName.valueOf(hubEvent.name)) {
                        AuthChannelEventName.SIGNED_IN -> {
                            updateUserData(true)
                            Log.i(TAG, "HUB : SIGNED_IN")
                        }
                        AuthChannelEventName.SIGNED_OUT -> {
                            updateUserData(false)
                            Log.i(TAG, "HUB : SIGNED_OUT")
                        }
                        else -> Log.i(TAG, """HUB EVENT:${hubEvent.name}""")
                    }
                }
            }
        }

        Log.i(TAG, "retrieving session status")


        Amplify.Auth.fetchAuthSession(
            {
                val session = it as AWSCognitoAuthSession
                when (session.identityIdResult.type) {
                    AuthSessionResult.Type.SUCCESS ->
                        Log.i("AuthQuickStart", "IdentityId = ${session.identityIdResult.value}")
                    AuthSessionResult.Type.FAILURE ->
                        Log.w(
                            "AuthQuickStart",
                            "IdentityId not found",
                            session.identityIdResult.error
                        )
                }
            },
            { Log.e("AuthQuickStart", "Failed to fetch session", it) }
        )


        return this
    }

    // The remaining code change tracks the status of a user (are they signed in or not?) and
    // triggers the SignIn / SignUp user interface when a user click on a lock icon.

    /*
    When an authentication event is received, we call the updateUserData() method. This method keeps the UserData object in sync. The UserData.isSignedIn property is a LiveData<Boolean>, it means Observers that are subscribed to this property will be notified when the value changes. We use this mechanism to refresh the user interface automatically.
    You can learn more about LiveData in the Android doc. https://developer.android.com/topic/libraries/architecture/livedata

     We also add code to check previous authentication status at application startup time. When the application starts,
     it checks if a Cognito session already exists and updates the UserData accordingly.
     */
    // change our internal state and query list of notes
    private fun updateUserData(withSignedInStatus: Boolean) {
        UserData.setSignedIn(withSignedInStatus)

        val notes = UserData.notes().value
        val isEmpty = notes?.isEmpty() ?: false

        // query notes when signed in and we do not have Notes yet
        if (withSignedInStatus && isEmpty) {
            this.queryNotes()
        } else {
            UserData.resetNotes()
        }
    }

    fun signOut() {
        Log.i(TAG, "Initiate Signout Sequence")

//        Amplify.Auth.signOut(
//            { Log.i(TAG, "Signed out!") },
//            { error -> Log.e(TAG, error.toString()) }
//        )

        Amplify.Auth.signOut { signOutResult ->
            when (signOutResult) {
                is AWSCognitoAuthSignOutResult.CompleteSignOut -> {
                    // Sign Out completed fully and without errors.
                    Log.i("AuthQuickStart", "Signed out successfully")
                }
                is AWSCognitoAuthSignOutResult.PartialSignOut -> {
                    // Sign Out completed with some errors. User is signed out of the device.
                    signOutResult.hostedUIError?.let {
                        Log.e("AuthQuickStart", "HostedUI Error", it.exception)
                        // Optional: Re-launch it.url in a Custom tab to clear Cognito web session.

                    }
                    signOutResult.globalSignOutError?.let {
                        Log.e("AuthQuickStart", "GlobalSignOut Error", it.exception)
                        // Optional: Use escape hatch to retry revocation of it.accessToken.
                    }
                    signOutResult.revokeTokenError?.let {
                        Log.e("AuthQuickStart", "RevokeToken Error", it.exception)
                        // Optional: Use escape hatch to retry revocation of it.refreshToken.
                    }
                }
                is AWSCognitoAuthSignOutResult.FailedSignOut -> {
                    // Sign Out failed with an exception, leaving the user signed in.
                    Log.e("AuthQuickStart", "Sign out Failed", signOutResult.exception)
                }
            }
        }
    }


    fun getOtherUsers()
    {
        //Amplify.Auth.user
    }
    fun signIn(callingActivity: Activity) {
        Log.i(TAG, "Initiate Signin Sequence")

        // if signed in, sign out first
        Amplify.Auth.getCurrentUser(
            {
                signOut()
                Amplify.Auth.signInWithWebUI(
                    callingActivity,
                    { result: AuthSignInResult -> Log.i(TAG, result.toString()) },
                    { error: AuthException -> Log.e(TAG, error.toString()) }
                )
                createIfProfileNotExist()
            }, {
                Amplify.Auth.signInWithWebUI(
                    callingActivity,
                    { result: AuthSignInResult -> Log.i(TAG, result.toString()) },
                    { error: AuthException -> Log.e(TAG, error.toString()) }
                )
                createIfProfileNotExist()
            }
        )
    }

    fun createIfProfileNotExist(){
        // query and check if there already exist a note
        Amplify.API.query(
            ModelQuery.list(NoteData::class.java),
            { response ->
                Log.i(TAG, "Queried")
                if(response.data.items.count() < 1){
                    // create a note object
                    val note = UserData.Note(
                        UUID.randomUUID().toString(),
                        "deon",
                        "deon",
                        "deon",
                        "deon",
                        "deon",
                        "deon",
                        0,
                        0
                    )

//                    // addNoteACtivity.kt, inside the addNote.setOnClickListener() method and after the Note() object is created.
//                    if (this.noteImagePath != null) {
//                        note.imageName = UUID.randomUUID().toString()
//                        //note.setImage(this.noteImage)
//                        note.image = this.noteImage
//
//                        // asynchronously store the image (and assume it will work)
//                        Backend.storeImage(this.noteImagePath!!, note.imageName!!)
//                    }

                    // store it in the backend
                    Backend.createNote(note)

                    // add it to UserData, this will trigger a UI refresh
                    UserData.addNote(note)
                }
            },
            { error -> Log.e(TAG, "Query failure", error) }
        )
    }

    // pass the data from web redirect to Amplify libs
    fun handleWebUISignInResponse(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(TAG, "received requestCode : $requestCode and resultCode : $resultCode")
        //if (requestCode == AWSCognitoAuthPlugin.WEB_UI_SIGN_IN_ACTIVITY_CODE) {
        Log.d("main", "entered here!!!!!!")
        Amplify.Auth.handleWebUISignInResponse(data)
        //}
    }

    fun queryNotes() {
        Log.i(TAG, "Querying notes")

        Amplify.API.query(
            ModelQuery.list(NoteData::class.java),
            { response ->
                Log.i(TAG, "Queried")
                for (noteData in response.data) {
                    Log.i(TAG, noteData.name)
                    // TODO should add all the notes at once instead of one by one (each add triggers a UI refresh)
                    UserData.addNote(UserData.Note.from(noteData))
                }
            },
            { error -> Log.e(TAG, "Query failure", error) }
        )
    }

    fun createNote(note: UserData.Note) {
        Log.i(TAG, "Creating notes")

        Amplify.API.mutate(
            ModelMutation.create(note.data),
            { response ->
                Log.i(TAG, "Created")
                if (response.hasErrors()) {
                    Log.e(TAG, response.errors.first().message)
                } else {
                    Log.i(TAG, "Created Note with id: " + response.data.id)
                }
            },
            { error -> Log.e(TAG, "Create failed", error) }
        )
    }

    fun editNote(note: UserData.Note) {
        Log.i(TAG, "Updating notes")

        Amplify.API.mutate(
            ModelMutation.update(note.data),
            { response ->
                Log.i(TAG, "Updating")
                if (response.hasErrors()) {
                    Log.e(TAG, response.errors.first().message)
                } else {
                    Log.i(TAG, "Updating Note with id: " + response.data.id)
                }
            },
            { error -> Log.e(TAG, "Updating failed", error) }
        )
    }

    fun deleteNote(note: UserData.Note?) {

        if (note == null) return

        Log.i(TAG, "Deleting note $note")

        Amplify.API.mutate(
            ModelMutation.delete(note.data),
            { response ->
                Log.i(TAG, "Deleted")
                if (response.hasErrors()) {
                    Log.e(TAG, response.errors.first().message)
                } else {
                    Log.i(TAG, "Deleted Note $response")
                }
            },
            { error -> Log.e(TAG, "Delete failed", error) }
        )
    }

    // three methods to upload, download and delete image from the Storage:
    /*
    These three methods simply call their Amplify counterpart. Amplify storage has three file protection levels:

    Public Accessible by all users
    Protected Readable by all users, but only writable by the creating user
    Private Readable and writable only by the creating user
    For this app, we want the images to only be available to the Note owner, we are using the StorageAccessLevel.PRIVATE property.
     */
    fun storeImage(filePath: String, key: String) {
        val file = File(filePath)
        val options = StorageUploadFileOptions.builder()
            .accessLevel(StorageAccessLevel.PRIVATE)
            .build()

        Amplify.Storage.uploadFile(
            key,
            file,
            options,
            { progress -> Log.i(TAG, "Fraction completed: ${progress.fractionCompleted}") },
            { result -> Log.i(TAG, "Successfully uploaded: " + result.key) },
            { error -> Log.e(TAG, "Upload failed", error) }
        )
    }

    fun deleteImage(key: String) {

        val options = StorageRemoveOptions.builder()
            .accessLevel(StorageAccessLevel.PRIVATE)
            .build()

        Amplify.Storage.remove(
            key,
            options,
            { result -> Log.i(TAG, "Successfully removed: " + result.key) },
            { error -> Log.e(TAG, "Remove failure", error) }
        )
    }

    fun retrieveImage(key: String, completed: (image: Bitmap) -> Unit) {
        val options = StorageDownloadFileOptions.builder()
            .accessLevel(StorageAccessLevel.PRIVATE)
            .build()

        val file = File.createTempFile("image", ".image")

        Amplify.Storage.downloadFile(
            key,
            file,
            options,
            { progress -> Log.i(TAG, "Fraction completed: ${progress.fractionCompleted}") },
            { result ->
                Log.i(TAG, "Successfully downloaded1: ${result.file.name}")
                val imageStream = FileInputStream(file)
                val image = BitmapFactory.decodeStream(imageStream)
                completed(image)
            },
            { error -> Log.e(TAG, "Download Failure", error) }
        )
    }
}