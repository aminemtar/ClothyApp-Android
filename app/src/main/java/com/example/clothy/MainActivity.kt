package com.example.clothy

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.clothy.ui.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {
    private lateinit var add:FloatingActionButton

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val isLoggedIn = preferences?.contains("email")

        if (isLoggedIn!!) {
            loadHomeFragment()

        }else{
            startActivity(Intent(this,LoginActivity::class.java))

        }
        // Move the code here
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
bottomNavigationView.background= null
        bottomNavigationView.menu.getItem(2).isEnabled=false
        bottomNavigationView.menu.findItem(R.id.nav_home).isChecked = true
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> replaceFragment(CategoryFragment())
                R.id.nav_shop -> replaceFragment(MatchFragment())
                R.id.nav_profile -> replaceFragment(ProfileFragment())
                R.id.nav_cart ->replaceFragment(HomeFragment())
            }
            true
        }
        add= findViewById(R.id.Addoutfit)
        add.setOnClickListener {
            startActivity(Intent(this,OpenCamera::class.java))
        }

    }
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.host, fragment)
            .commit()
    }
     private fun loadHomeFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.host, CategoryFragment())
            .commit()
    }


}