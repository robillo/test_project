package com.robillo.test_project.view.launcher

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

import com.robillo.test_project.R
import com.robillo.test_project.view.contacts.ContactsActivity
import com.robillo.test_project.view.main.MainActivity

class LauncherActivity : AppCompatActivity() {

    internal lateinit var partOne: Button
    internal lateinit var partTwo: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)

        partOne = findViewById(R.id.part1)
        partTwo = findViewById(R.id.part2)

        partOne.setOnClickListener { startActivity(Intent(this@LauncherActivity, MainActivity::class.java)) }

        partTwo.setOnClickListener { startActivity(Intent(this@LauncherActivity, ContactsActivity::class.java)) }
    }
}
