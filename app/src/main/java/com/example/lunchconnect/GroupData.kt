package com.example.lunchconnect
/*
 * Copyright 2022 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

class GroupData : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // List the available demos
        val listView = ListView(this).also {
            it.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            it.adapter = DemoAdapter(this, GMapDemo.values())
            it.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
                val demo = parent.adapter.getItem(position) as? GMapDemo
                demo?.let {
                    startActivity(Intent(this, demo.activity))
                }
            }
        }
        setContentView(listView)
    }

    private class DemoAdapter(context: Context, demos: Array<GMapDemo>) :
            ArrayAdapter<GMapDemo>(context, R.layout.activity_group_data, demos) {

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
            LayoutInflater.from(context)
                .inflate(R.layout.activity_group_data, this)
        }
    }
}