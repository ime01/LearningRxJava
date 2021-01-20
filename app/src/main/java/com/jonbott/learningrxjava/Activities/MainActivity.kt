package com.jonbott.learningrxjava.Activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import com.jonbott.learningrxjava.Activities.DatabaseExample.DatabaseExampleActivity
import com.jonbott.learningrxjava.Activities.NetworkExample.NetworkExampleActivity
import com.jonbott.learningrxjava.Activities.ReactiveUi.Complex.ReactiveUIActivity
import com.jonbott.learningrxjava.Activities.ReactiveUi.Simple.SimpleUIActivity
import com.jonbott.learningrxjava.Activities.TasksExample.TasksExampleActivity
import com.jonbott.learningrxjava.Activities.ThreadingExample.ThreadingExampleActivity
import com.jonbott.learningrxjava.R
import kotlinx.android.synthetic.main.activity_main.*

enum class Example(val label: String) {
    BASIC("Basic Example"),
    DATABASE("Database Example"),
    NETWORK("Network Example"),
    TASKS("Tasks Example"),
    SIMPLE_UI("Simple UI Example"),
    COMPLEX_UI("Complex UI Example"),
    THREADING("Threading"),
}

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupList()
    }

    private fun setupList() {
        val listItems = Example.values().map { it.label }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems)
        activitiesListView.adapter = adapter

        activitiesListView.setOnItemClickListener { parent, view, position, id ->
            when(position) {
                Example.BASIC.ordinal -> presentBasicExample()
                Example.DATABASE.ordinal -> presentDatabaseExample()
                Example.NETWORK.ordinal -> presentNetworkExample()
                Example.TASKS.ordinal -> presentTasksExample()
                Example.SIMPLE_UI.ordinal -> presentSimpleUIExample()
                Example.COMPLEX_UI.ordinal -> presentComplexUIExample()
                Example.THREADING.ordinal -> presentThreadingExample()
            }
        }
    }

    private fun presentBasicExample() {
        val intent = Intent(this, BasicExampleActivity::class.java)
        startActivity(intent)
    }

    private fun presentDatabaseExample() {
        val intent = Intent(this, DatabaseExampleActivity::class.java)
        startActivity(intent)
    }

    private fun presentNetworkExample() {
        val intent = Intent(this, NetworkExampleActivity::class.java)
        startActivity(intent)
    }

    private fun presentTasksExample() {
        val intent = Intent(this, TasksExampleActivity::class.java)
        startActivity(intent)
    }

    private fun presentSimpleUIExample() {
        val intent = Intent(this, SimpleUIActivity::class.java)
        startActivity(intent)
    }

    private fun presentComplexUIExample() {
        val intent = Intent(this, ReactiveUIActivity::class.java)
        startActivity(intent)
    }

    private fun presentThreadingExample() {
        val intent = Intent(this, ThreadingExampleActivity::class.java)
        startActivity(intent)
    }
}
