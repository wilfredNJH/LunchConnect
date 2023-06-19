package com.example.lunchconnect

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import org.w3c.dom.Text

class PointsAndBadges : AppCompatActivity() {

    // here's where can link cloud data to the page
    private fun initializeUserData() {
        // set some points
        setPoints(getPoints())
        // some code to load badges from database
        addBadge(R.drawable.badge_heart, "its a heart")
        addBadge(R.drawable.badge_chest, "this a bomb")
        addBadge(R.drawable.badge_flaggreen, "this a flag")
    }

    // back button
    private lateinit var backButton: ImageButton

    // progress bar
    // read tier map as: p < 100 is tier 1, 100 < p < 200 is tier 2
    val tierMap = mapOf(1 to 100, 2 to 200, 3 to 300, 4 to 400)
    private lateinit var progressBar : ProgressBar
    private lateinit var progressDescription : TextView
    private lateinit var pointsDisplay : TextView
    private lateinit var currentTier : TextView
    private lateinit var nextTier : TextView

    // badge container
    private lateinit var badgeContainer : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_points_and_badges)

        // back button goes back to the main activity
        backButton = findViewById(R.id.ib_backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // sets progress bar based on users points
        progressBar = findViewById(R.id.pb_pointsTier)
        progressDescription = findViewById(R.id.tv_progressDescription)
        pointsDisplay = findViewById(R.id.tv_pointsDisplay)
        currentTier = findViewById(R.id.tv_currentTier)
        nextTier = findViewById(R.id.tv_nextTier)

        // loads all the users badges
        badgeContainer = findViewById(R.id.ll_badgeContainer)

        initializeUserData()
    }

    private fun addBadge(img: Int, desc: String) {
        val inflater = LayoutInflater.from(this)
        val badgeLayout = inflater.inflate(R.layout.badge_layout, badgeContainer, false)
        val badge = badgeLayout.findViewById<ImageView>(R.id.badge)
        badge.setImageResource(img)
        val badgeDescription = badgeLayout.findViewById<TextView>(R.id.badgeDescription)
        badgeDescription.text = desc
        badgeContainer.addView(badgeLayout)
    }

    private fun getPoints() : Int {
        return 245
    }

    private fun setPoints(points: Int) {
        pointsDisplay.text = points.toString()
        // check which tier the points fall within
        var thisTier = 1
        var pointsThisTier = 0
        tierMap.forEach { tier ->
            if (points > tier.value) {
                ++thisTier
                pointsThisTier = tier.value
            }
        }
        currentTier.text = "TIER " + thisTier.toString()
        nextTier.text = "TIER " + (thisTier+1).toString()
        // calculate points to next tier
        var pointsOfNextTier =  tierMap[thisTier+1]
        var pointsOfThisTier = tierMap[thisTier]
        var pointsToNextTier = (pointsOfThisTier?:points)-points
        progressBar.max = (pointsOfNextTier?:0) - (pointsOfThisTier?:0)
        Log.d("tag", progressBar.max.toString())
        progressDescription.text = String.format(getString(R.string.points_and_badges_tier_progress),pointsToNextTier,thisTier+1)
        progressBar.progress = points - pointsThisTier
    }
}