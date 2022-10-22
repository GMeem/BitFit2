package com.example.sleepbit2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentManager: FragmentManager = supportFragmentManager

        val fragment1: Fragment = LogFragment()
        val fragment2: Fragment = DashboardFragment()

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNavigationView.setOnItemSelectedListener { item ->
            lateinit var fragment: Fragment
            when (item.itemId) {
                R.id.nav_Log -> fragment = fragment1
                R.id.nav_Dashboard -> fragment = fragment2

            }
            fragmentManager.beginTransaction().replace(R.id.rlContainer, fragment).commit()
            true
        }

        // Set default selection
        bottomNavigationView.selectedItemId = R.id.nav_Log

        findViewById<Button>(R.id.createBtn).setOnClickListener{
            val intent = Intent(this@MainActivity, SleepEntryActivity::class.java)
            startActivity(intent)
        }
    }
}