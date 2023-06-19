package com.example.lunchconnect

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.TextView

class QuestionAdapter(context: Context, questions: ArrayList<String>) :
    ArrayAdapter<String>(context, 0, questions) {

    var checkedItems: ArrayList<Boolean> = ArrayList()

    init {
        // Initialize the checkedItems list with default values
        for (i in 0 until questions.size) {
            checkedItems.add(false)
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.content_questionnaire_item, parent, false)
        }

        val questionText = itemView?.findViewById<TextView>(R.id.questionText)
        val checkBox = itemView?.findViewById<CheckBox>(R.id.checkBox)

        val question = getItem(position)
        questionText?.text = question
        checkBox?.isChecked = checkedItems[position]

        checkBox?.setOnClickListener {
            checkedItems[position] = checkBox.isChecked
        }

        return itemView!!
    }
}
