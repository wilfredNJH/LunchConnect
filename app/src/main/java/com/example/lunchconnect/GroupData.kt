package com.example.lunchconnect

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.collections.ArrayList

class GroupData : AppCompatActivity() {

    private lateinit var groupDescriptionEditText: EditText
    private lateinit var timeStartEditText: EditText
    private lateinit var timeEndEditText: EditText
    private lateinit var eatingLocationEditText: EditText
    private lateinit var meetingLocationEditText: EditText
    private lateinit var specialRequestEditText: EditText
    private lateinit var createGroupButton: Button
    private lateinit var groupListView: ListView
    private lateinit var groupListAdapter: ArrayAdapter<String>
    private var groupList: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_data)

        groupDescriptionEditText = findViewById(R.id.groupDescriptionEditText)
        timeStartEditText = findViewById(R.id.timeStartEditText)
        timeEndEditText = findViewById(R.id.timeEndEditText)
        eatingLocationEditText = findViewById(R.id.eatingLocationEditText)
        meetingLocationEditText = findViewById(R.id.meetingLocationEditText)
        specialRequestEditText = findViewById(R.id.specialRequestEditText)
        createGroupButton = findViewById(R.id.createGroupButton)
        groupListView = findViewById(R.id.groupListView)

        createGroupButton.setOnClickListener {
            createGroup()
        }

        groupListAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, groupList)
        groupListView.adapter = groupListAdapter

        val demoAdapter = DemoAdapter(this, GMapDemo.values())
        groupListView.adapter = demoAdapter
        groupListView.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            val demo = parent.adapter.getItem(position) as? GMapDemo
            demo?.let {
                startActivity(Intent(this, demo.activity))
            }
        }
    }

    private class DemoAdapter(context: Context, demos: Array<GMapDemo>) :
            ArrayAdapter<GMapDemo>(context, R.layout.demo_item_view, demos) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val demoView = (convertView as? DemoItemView) ?: DemoItemView(context)
            return demoView.also {
                val demo = getItem(position)
                it.title.setText(demo?.title ?: 0)
                it.description.setText(demo?.description ?: 0)
            }
        }
    }

    private class DemoItemView(context: Context) : LinearLayout(context) {
        val title: TextView by lazy { findViewById(R.id.textViewTitle) }
        val description: TextView by lazy { findViewById(R.id.textViewDescription) }

        init {
            LayoutInflater.from(context).inflate(R.layout.demo_item_view, this)
        }
    }

    private fun createGroup() {
        val groupDescription = groupDescriptionEditText.text.toString()
        val timeStart = timeStartEditText.text.toString()
        val timeEnd = timeEndEditText.text.toString()
        val eatingLocation = eatingLocationEditText.text.toString()
        val meetingLocation = meetingLocationEditText.text.toString()
        val specialRequest = specialRequestEditText.text.toString()

        // Check if any required field is empty
        if (groupDescription.isEmpty() || timeStart.isEmpty() || timeEnd.isEmpty() || eatingLocation.isEmpty() || meetingLocation.isEmpty()) {
            Toast.makeText(this, "Please fill in all the required information", Toast.LENGTH_SHORT).show()
            return
        }

        val groupSummary =
            "Description: $groupDescription\nTime: $timeStart - $timeEnd\nEating Location: $eatingLocation\nMeeting Location: $meetingLocation\nSpecial Requests: $specialRequest"

        // Log the group summary to Logcat
        Log.d("GroupSummary", groupSummary)

//        groupList.add(groupSummary)
//        groupListAdapter.notifyDataSetChanged()
        // add group to database
        val group = UserGroupData.GroupNote(
            UUID.randomUUID().toString(),
            mutableListOf(""),
            "Yishun",
            "",
            ""
        )

        // store it in the backend
        Backend.createGroup(group)

        // add it to UserGroupData, this will trigger a UI refresh
        UserGroupData.addGroup(group)

        // Clear input fields
        groupDescriptionEditText.text.clear()
        timeStartEditText.text.clear()
        timeEndEditText.text.clear()
        eatingLocationEditText.text.clear()
        meetingLocationEditText.text.clear()
        specialRequestEditText.text.clear()
    }
}