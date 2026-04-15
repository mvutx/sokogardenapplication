package com.example.sokogarden

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
//        find the buttons  by use of their ids
        val signupButton = findViewById<Button>(R.id.signupBtn)
        val signinButton = findViewById<Button>(R.id.signinBtn)

        val session = SessionManager(this)

// If logged in → change Signup button to Logout
        if (session.isLoggedIn()) {
            signupButton.text = "🚪 Logout"
        }
//        create an intent to the two activities
        signupButton.setOnClickListener {
            val intent = Intent(applicationContext, Signup::class.java)
            startActivity(intent)
        }
        //        =====================================
        signinButton.setOnClickListener {
            val intent = Intent(applicationContext, Signin::class.java)
            startActivity(intent)
        }
        signupButton.setOnClickListener {

            val session = SessionManager(this)

            if (session.isLoggedIn()) {
                // LOGOUT
                session.logout()

                val intent = Intent(this, Signin::class.java)
                startActivity(intent)
                finish()

            } else {
                // NORMAL SIGNUP FLOW
                val intent = Intent(this, Signup::class.java)
                startActivity(intent)
            }
        }
        if (session.isLoggedIn()) {
            signinButton.visibility = View.GONE
        }

//        find the recyclerview and the progressbar by use of their ids
        val recyclerview = findViewById<RecyclerView>(R.id.recyclerview)
        val progressbar = findViewById<ProgressBar>(R.id.progressbar)
//         specify the API url for fetching the products (alwaysdata)
        val url = "https://kbenkamotho.alwaysdata.net/api/get_products"

//        import the helper class
        val helper = ApiHelper(applicationContext)

//        inside the helper class access the function loadproducts
        helper.loadProducts(url, recyclerview, progressbar)
    }
}