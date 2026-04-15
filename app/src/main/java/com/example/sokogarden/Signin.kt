package com.example.sokogarden

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.loopj.android.http.RequestParams

class Signin : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 🔥 AUTO LOGIN (ADDED - does not remove anything)
        val sessionCheck = SessionManager(this)
        if (sessionCheck.isLoggedIn()) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        enableEdgeToEdge()
        setContentView(R.layout.activity_signin)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // UI elements
        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)
        val signinButton = findViewById<Button>(R.id.signinBtn)
        val signuptxt = findViewById<TextView>(R.id.signuptxt)

        // Navigate to Signup
        signuptxt.setOnClickListener {
            val intent = Intent(applicationContext, Signup::class.java)
            startActivity(intent)
        }

        // SIGNIN BUTTON CLICK
        signinButton.setOnClickListener {

            val api = "https://kbenkamotho.alwaysdata.net/api/signin"

            val data = RequestParams()
            data.put("email", email.text.toString())
            data.put("password", password.text.toString())

            val helper = ApiHelper(applicationContext)

            // ✅ ONLY LOGIN + REDIRECT ON SUCCESS
            helper.post_login(api, data) { response ->

                if (response != null) {

                    val session = SessionManager(this)
                    session.login()

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()

                } else {
                    // ❌ Do nothing → stay on login screen
                }
            }
        }
    }
}