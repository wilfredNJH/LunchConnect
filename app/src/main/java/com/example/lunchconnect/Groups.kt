package com.example.lunchconnect

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import kotlin.random.Random

class Groups : AppCompatActivity() {

    private fun initialize() {
        // add random groups for now
        addGroup("Manegerial Munchers", "Karen", 13.00f, 14.00f, "MALA", "Gossip about the newbies in the company.")
        addGroup("Friendly Group", "Ragesh", 13.30f, 15.00f, "MEXICAN", "All are welcome, lets have a nice meal. Eating mexican at gyg.")
        addGroup("Team Lead Lunch Bunch", "Yi Chun", 12.00f, 14.00f, "COFFEE SHOP", "Need help with challenges faced in current project.")
        addGroup("Manegerial Munchers", "Karen", 13.00f, 14.00f, "MALA", "Gossip about the newbies in the company.")
        addGroup("Friendly Group", "Ragesh", 13.30f, 15.00f, "MEXICAN", "All are welcome, lets have a nice meal. Eating mexican at gyg.")
        addGroup("Team Lead Lunch Bunch", "Yi Chun", 12.00f, 14.00f, "COFFEE SHOP", "Need help with challenges faced in current project.")
    }

    private lateinit var groupContainer : LinearLayout

    val memberPics = arrayOf(R.drawable.people_01,R.drawable.people_02,R.drawable.people_03,R.drawable.people_04,R.drawable.people_05,R.drawable.people_06)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_groups)

        // set back button to main activity
        findViewById<ImageButton>(R.id.ib_groupsBackButton).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // set add group button
        findViewById<ImageButton>(R.id.ib_groupsAddGroup).setOnClickListener {
            val intent = Intent(this, GroupData::class.java)
            startActivity(intent)
        }

        groupContainer = findViewById(R.id.ll_groupsContainer)

        // set a image of a fake map
        val imageView = findViewById<ImageView>(R.id.iv_groupsMapScreenShot)
        imageView.setOnClickListener {
            // go to actual map
            val intent = Intent(this, GMapActivity::class.java)
            startActivity(intent)
        }

        // initialize
        initialize()
    }

    private fun addGroup(groupName:String,creatorName:String,timeStart:Float,timeEnd:Float,foodGenre:String,desc:String) {
        val inflater = LayoutInflater.from(this)
        val groupLayout = inflater.inflate(R.layout.groups_layout, groupContainer, false)

        // set group attributes
        groupLayout.findViewById<TextView>(R.id.tv_groupsCardGroupName).text = groupName
        groupLayout.findViewById<TextView>(R.id.tv_groupsCardCreator).text = "Created by " + creatorName
        groupLayout.findViewById<TextView>(R.id.tv_groupsCardFoodGenre).text = foodGenre
        groupLayout.findViewById<TextView>(R.id.tv_groupsCardTime).text = "%.2f".format(timeStart) + "-" + "%.2f".format(timeEnd)
        groupLayout.findViewById<TextView>(R.id.tv_groupsCardDescription).text = desc

        // set info button event to hide and show hidden info
        val infoButton = groupLayout.findViewById<Button>(R.id.btn_groupsInfo)
        val hiddenInfo = groupLayout.findViewById<LinearLayout>(R.id.ll_groupsHiddenInfo)
        hiddenInfo.visibility = View.GONE
        infoButton.setOnClickListener {
            if (hiddenInfo.visibility == View.GONE) {
                hiddenInfo.visibility = View.VISIBLE
            } else {
                hiddenInfo.visibility = View.GONE
            }
        }

        // randomly add existing members of groups to card for now
        val memberContainer = groupLayout.findViewById<LinearLayout>(R.id.ll_groupsCardMembers)
        for (i in 0..Random.nextInt(0,4)) {
            val memberLayout = inflater.inflate(R.layout.rounded_profile_pic, memberContainer, false)
            memberLayout.findViewById<ImageView>(R.id.iv_roundedProfilePic).setImageResource(memberPics[Random.nextInt(0,5)])
            memberContainer.addView(memberLayout)
        }

        groupContainer.addView(groupLayout)
    }
}