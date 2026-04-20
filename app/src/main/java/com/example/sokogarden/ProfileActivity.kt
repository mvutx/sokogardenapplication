package com.example.sokogarden

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlin.jvm.java

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // ================= VIEW REFERENCES =================
        val profileImage = findViewById<ImageView>(R.id.profileImage)

        val displayUsername = findViewById<TextView>(R.id.displayUsername)
        val displayEmail = findViewById<TextView>(R.id.displayEmail)

        val usernameEdit = findViewById<EditText>(R.id.usernameEdit)
        val emailEdit = findViewById<EditText>(R.id.emailEdit)
        val passwordEdit = findViewById<EditText>(R.id.passwordEdit)
        val phoneEdit = findViewById<EditText>(R.id.phoneEdit)

        val saveUsernameBtn = findViewById<Button>(R.id.saveUsernameBtn)
        val saveEmailBtn = findViewById<Button>(R.id.saveEmailBtn)
        val savePasswordBtn = findViewById<Button>(R.id.savePasswordBtn)
        val savePhoneBtn = findViewById<Button>(R.id.savePhoneBtn)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        // ================= SHARED PREFS =================
        val prefs = getSharedPreferences("user_session", MODE_PRIVATE)
        val editor = prefs.edit()

        // ================= LOAD DATA =================
        val username = prefs.getString("username", "john_doe")
        val email = prefs.getString("email", "johndoe@email.com")
        val password = prefs.getString("password", "")
        val phone = prefs.getString("phone", "")

        usernameEdit.setText(username)
        emailEdit.setText(email)
        passwordEdit.setText(password)
        phoneEdit.setText(phone)

        displayUsername.text = username
        displayEmail.text = email

        // ================= SAVE ACTIONS =================
        saveUsernameBtn.setOnClickListener {
            val newUsername = usernameEdit.text.toString()
            editor.putString("username", newUsername).apply()
            displayUsername.text = newUsername
            Toast.makeText(this, "Username Updated ✅", Toast.LENGTH_SHORT).show()
        }

        saveEmailBtn.setOnClickListener {
            val newEmail = emailEdit.text.toString()
            editor.putString("email", newEmail).apply()
            displayEmail.text = newEmail
            Toast.makeText(this, "Email Updated ✅", Toast.LENGTH_SHORT).show()
        }

        savePasswordBtn.setOnClickListener {
            editor.putString("password", passwordEdit.text.toString()).apply()
            Toast.makeText(this, "Password Updated ✅", Toast.LENGTH_SHORT).show()
        }

        savePhoneBtn.setOnClickListener {
            editor.putString("phone", phoneEdit.text.toString()).apply()
            Toast.makeText(this, "Phone Updated ✅", Toast.LENGTH_SHORT).show()
        }

        // ================= PROFILE IMAGE CLICK =================
        profileImage.setOnClickListener {
            Toast.makeText(this, "Change profile photo (not implemented)", Toast.LENGTH_SHORT).show()
        }

        // ================= BOTTOM NAVIGATION (UPDATED) =================
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {

                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                    true
                }

                R.id.nav_settings -> {
                    startActivity(Intent(this, SessionManager::class.java))
                    finish()
                    true
                }

                else -> false
            }
        }
    }
}