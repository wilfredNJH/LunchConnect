package com.example.lunchconnect

/*
a layout xml file that describe the layout of component on a cell representing a Note. It displays the image, the note title, and the note description.
a support Kotlin class. It receives a Note data object at creation time and assign individual values to their corresponding views (image, title and description)
 */
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// this is a single cell (row) in the list of Groups
class GroupRecyclerViewAdapter(
    private val values: MutableList<UserGroupData.GroupNote>?) :
    RecyclerView.Adapter<GroupRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.content_note, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = values?.get(position)
        holder.nameView.text = item?.location
        holder.descriptionView.text = item?.specialRequest

    }

    override fun getItemCount() = values?.size ?: 0

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.image)
        val nameView: TextView = view.findViewById(R.id.name)
        val descriptionView: TextView = view.findViewById(R.id.description)
    }
}