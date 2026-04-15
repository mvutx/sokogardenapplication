package com.example.sokogarden

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.loopj.android.http.RequestParams

class Signin : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signin)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Existing views
        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)
        val signinButton = findViewById<Button>(R.id.signinBtn)
        val signuptxt = findViewById<TextView>(R.id.signuptxt)

        // NEW views (from XML we added)
        val toggle = findViewById<TextView>(R.id.togglePassword)
        val loader = findViewById<ProgressBar>(R.id.loading)
        val emailError = findViewById<TextView>(R.id.emailError)
        val passError = findViewById<TextView>(R.id.passwordError)

        // 👁️ Show / Hide password
        toggle.setOnClickListener {
            if (password.inputType == (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                password.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                password.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            password.setSelection(password.text.length)
        }

        // Navigate to Signup
        signuptxt.setOnClickListener {
            val intent = Intent(applicationContext, Signup::class.java)
            startActivity(intent)
        }

        // Signin button logic
        signinButton.setOnClickListener {

            // Hide errors first
            emailError.visibility = View.GONE
            passError.visibility = View.GONE

            val emailTxt = email.text.toString()
            val passTxt = password.text.toString()

            var valid = true

            // ❗ Validation
            if (emailTxt.isEmpty()) {
                emailError.visibility = View.VISIBLE
                valid = false
            }

            if (passTxt.isEmpty()) {
                passError.visibility = View.VISIBLE
                valid = false
            }

            if (!valid) return@setOnClickListener

            // 🔄 Show loader
            loader.visibility = View.VISIBLE
            signinButton.text = ""

            // API endpoint
            val api = "https://kbenkamotho.alwaysdata.net/api/signin"

            val data = RequestParams()
            data.put("email", emailTxt)
            data.put("password", passTxt)

            val helper = ApiHelper(applicationContext)

            // Simulate loading (optional but nice UX)
            Handler(Looper.getMainLooper()).postDelayed({
                loader.visibility = View.GONE
                signinButton.text = "Sign In"

                // Call API AFTER loader (or move this above if you want real-time)
                helper.post_login(api, data)

            }, 1500)
        }
    }
}