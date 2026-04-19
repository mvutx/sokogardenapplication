package com.example.sokogarden

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 🔐 AUTO LOGIN PROTECTION
        val sessionCheck = SessionManager(this)
        if (!sessionCheck.isLoggedIn()) {
            startActivity(Intent(this, Signin::class.java))
            finish()
            return
        }

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // UI ELEMENTS
        val signupButton = findViewById<Button>(R.id.signupBtn)
        val signinButton = findViewById<Button>(R.id.signinBtn)
        val welcomeUser = findViewById<TextView>(R.id.welcomeUser)

        val recyclerview = findViewById<RecyclerView>(R.id.recyclerview)
        val progressbar = findViewById<ProgressBar>(R.id.progressbar)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        val session = SessionManager(this)

        // 🔐 GET USER DATA
        val prefs = getSharedPreferences("user_session", MODE_PRIVATE)
        val username = prefs.getString("username", "Guest")

        // 🎯 UI STATE
        if (session.isLoggedIn()) {

            welcomeUser.text = "👋 Hello, $username"

            signupButton.visibility = View.GONE
            signinButton.visibility = View.GONE

        } else {
            welcomeUser.text = "Welcome 👋"
        }

        // 👤 SIGNUP
        signupButton.setOnClickListener {
            startActivity(Intent(this, Signup::class.java))
        }

        // 🔐 SIGNIN
        signinButton.setOnClickListener {
            startActivity(Intent(this, Signin::class.java))
        }

        // 🌐 LOAD PRODUCTS
        val url = "https://kbenkamotho.alwaysdata.net/api/get_products"
        val helper = ApiHelper(applicationContext)
        helper.loadProducts(url, recyclerview, progressbar)

        // =========================================
        // 🔥 BOTTOM NAVIGATION
        // =========================================

        bottomNav.setOnItemSelectedListener { item ->

            when (item.itemId) {

                R.id.nav_home -> {
                    Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }

                R.id.nav_logout -> {
                    session.logout()
                    prefs.edit().clear().apply()

                    Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()

                    startActivity(Intent(this, Signin::class.java))
                    finish()
                    true
                }

                else -> false
            }
        }

//        find the about button by use of its id and have the intent
        val aboutButton = findViewById<Button>(R.id.aboutbtn)
//        below s the intent to the about page
        aboutButton.setOnClickListener {
            val intent = Intent(applicationContext, About::class.java)
            startActivity(intent)
        }
    }
}