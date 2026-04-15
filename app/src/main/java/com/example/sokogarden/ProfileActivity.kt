package com.example.sokogarden

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val usernameEdit = findViewById<EditText>(R.id.usernameEdit)
        val emailEdit = findViewById<EditText>(R.id.emailEdit)
        val passwordEdit = findViewById<EditText>(R.id.passwordEdit)
        val phoneEdit = findViewById<EditText>(R.id.phoneEdit)

        val saveUsernameBtn = findViewById<Button>(R.id.saveUsernameBtn)
        val saveEmailBtn = findViewById<Button>(R.id.saveEmailBtn)
        val savePasswordBtn = findViewById<Button>(R.id.savePasswordBtn)
        val savePhoneBtn = findViewById<Button>(R.id.savePhoneBtn)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        val prefs = getSharedPreferences("user_session", MODE_PRIVATE)

        // 🔐 Load existing data
        usernameEdit.setText(prefs.getString("username", ""))
        emailEdit.setText(prefs.getString("email", ""))
        passwordEdit.setText(prefs.getString("password", ""))
        phoneEdit.setText(prefs.getString("phone", ""))

        val editor = prefs.edit()

        // ================= SAVE USERNAME =================
        saveUsernameBtn.setOnClickListener {
            editor.putString("username", usernameEdit.text.toString())
            editor.apply()
            Toast.makeText(this, "Username Updated ✅", Toast.LENGTH_SHORT).show()
        }

        // ================= SAVE EMAIL =================
        saveEmailBtn.setOnClickListener {
            editor.putString("email", emailEdit.text.toString())
            editor.apply()
            Toast.makeText(this, "Email Updated ✅", Toast.LENGTH_SHORT).show()
        }

        // ================= SAVE PASSWORD =================
        savePasswordBtn.setOnClickListener {
            editor.putString("password", passwordEdit.text.toString())
            editor.apply()
            Toast.makeText(this, "Password Updated ✅", Toast.LENGTH_SHORT).show()
        }

        // ================= SAVE PHONE =================
        savePhoneBtn.setOnClickListener {
            editor.putString("phone", phoneEdit.text.toString())
            editor.apply()
            Toast.makeText(this, "Phone Updated ✅", Toast.LENGTH_SHORT).show()
        }

        // ================= BOTTOM NAVIGATION =================
        bottomNav.setOnItemSelectedListener { item ->

            when (item.itemId) {

                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                    true
                }

                R.id.nav_profile -> {
                    Toast.makeText(this, "Already in Profile", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.nav_logout -> {
                    prefs.edit().clear().apply()
                    startActivity(Intent(this, Signin::class.java))
                    finish()
                    true
                }

                else -> false
            }
        }
    }
}