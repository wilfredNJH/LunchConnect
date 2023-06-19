package com.example.lunchconnect

import android.os.Bundle
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class Questionnaire : AppCompatActivity() {
    private var questionListView: ListView? = null
    private var questionAdapter: QuestionAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questionnaire)
        questionListView = findViewById(R.id.questionListView)

        // Create an ArrayList of questions
        val questions: ArrayList<String> = ArrayList()
        questions.add(getString(R.string.Questionnaire1))
        questions.add(getString(R.string.Questionnaire2))
        questions.add(getString(R.string.Questionnaire3))
        questions.add(getString(R.string.Questionnaire4))
        questions.add(getString(R.string.Questionnaire5))
        questions.add(getString(R.string.Questionnaire6))
        // Add more questions as needed

        // Create an instance of the QuestionAdapter
        questionAdapter = QuestionAdapter(this, questions)

        // Set the adapter on the ListView
        questionListView?.adapter = questionAdapter

        // Optionally, handle checkbox interactions
        questionListView?.onItemClickListener = OnItemClickListener { parent, view, position, id ->
            val isChecked: Boolean = questionAdapter?.checkedItems?.get(position) ?: false
            questionAdapter?.checkedItems?.set(position, !isChecked)
            questionAdapter?.notifyDataSetChanged()
        }
    }
}
