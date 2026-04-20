package com.example.sokogarden

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val prefs = getSharedPreferences("user_session", MODE_PRIVATE)
        val username = prefs.getString("username", null)
        val isLoggedIn = username != null

        // UI
        val signupButton = findViewById<Button>(R.id.signupBtn)
        val signinButton = findViewById<Button>(R.id.signinBtn)
        val welcomeUser = findViewById<TextView>(R.id.welcomeUser)

        val recyclerview = findViewById<RecyclerView>(R.id.recyclerview)
        val progressbar = findViewById<ProgressBar>(R.id.progressbar)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        // 👤 UI STATE
        if (isLoggedIn) {
            welcomeUser.text = "👋 Hello, $username"
            signupButton.visibility = View.GONE
            signinButton.visibility = View.GONE
        } else {
            welcomeUser.text = "Welcome 👋"
        }

        // 🔐 BUTTONS
        signupButton.setOnClickListener {
            startActivity(Intent(this, Signup::class.java))
        }

        signinButton.setOnClickListener {
            startActivity(Intent(this, Signin::class.java))
        }

        // 🌐 PRODUCTS
        val url = "https://kbenkamotho.alwaysdata.net/api/get_products"
        val helper = ApiHelper(applicationContext)
        helper.loadProducts(url, recyclerview, progressbar)

        // =========================
        // BOTTOM NAV
        // =========================
        bottomNav.setOnItemSelectedListener { item ->

            when (item.itemId) {

                R.id.nav_home -> {
                    Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.nav_settings -> {

                    val options = arrayOf("Profile", "About Us", "Logout")

                    AlertDialog.Builder(this)
                        .setTitle("Settings")
                        .setItems(options) { _, which ->

                            when (which) {

                                0 -> startActivity(Intent(this, ProfileActivity::class.java))

                                1 -> startActivity(Intent(this, About::class.java))

                                2 -> {
                                    prefs.edit().clear().apply()
                                    Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()

                                    startActivity(Intent(this, Signin::class.java))
                                    finish()
                                }
                            }
                        }
                        .show()

                    true
                }

                else -> false
            }
        }
    }
}