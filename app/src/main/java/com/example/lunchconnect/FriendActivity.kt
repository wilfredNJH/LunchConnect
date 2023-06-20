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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.FileNotFoundException

class FriendActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLayout()

        val friendsList = listOf(
            Friend("Alice", "Software Engineer",R.drawable.people_01),
            Friend("Bob", "Data Scientist",R.drawable.people_02),
            Friend("Charlie", "Product Manager",R.drawable.people_03),
            Friend("Daisy", "UI/UX Designer",R.drawable.people_04)
        )


        viewManager = LinearLayoutManager(this)
        viewAdapter = FriendAdapter(friendsList) // You will need to pass in your actual list of friends here.

        recyclerView = findViewById<RecyclerView>(R.id.recyclerView).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun setLayout() {

        setContentView(R.layout.activity_friend)






    }


    data class Friend(
        val name: String,
        val jobRole: String,
        val profileImage: Int // This assumes you will be R.drawables for images
    )
    class FriendViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.name)
        val jobRole: TextView = view.findViewById(R.id.job_role)
        val profileImage: ImageView = view.findViewById(R.id.profile_image)
    }

    class FriendAdapter(private val friends: List<Friend>) : RecyclerView.Adapter<FriendViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.friend_item, parent, false)
            return FriendViewHolder(view)
        }



        override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
            val friend = friends[position]
            holder.name.text = friend.name
            holder.jobRole.text = friend.jobRole
            //friend.profileImage?.let { holder.profileImage.setImageBitmap(it) }
            holder.profileImage.setImageResource(friend.profileImage)

        }

        override fun getItemCount() = friends.size
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


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
    private fun loadImage(filename: String, image: ImageView)
    {
        // Load the image from internal storage
        try {
            val bitmap = loadImageFromInternalStorage(filename)
            if (bitmap != null) {
                image.setImageBitmap(bitmap)
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            // Handle the situation when the file does not exist, perhaps by showing a default image or a toast message
        }
    }

    private fun saveData() {

    }
    private fun loadData() {

    }




}
