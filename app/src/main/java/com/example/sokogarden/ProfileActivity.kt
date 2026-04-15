package com.example.sokogarden

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val usernameEdit = findViewById<EditText>(R.id.usernameEdit)
        val emailEdit = findViewById<EditText>(R.id.emailEdit)
        val saveBtn = findViewById<Button>(R.id.saveBtn)

        val prefs = getSharedPreferences("user_session", MODE_PRIVATE)

        // 🔐 Load existing data
        usernameEdit.setText(prefs.getString("username", ""))
        emailEdit.setText(prefs.getString("email", ""))

        // 💾 Save updates
        saveBtn.setOnClickListener {
            val editor = prefs.edit()

            editor.putString("username", usernameEdit.text.toString())
            editor.putString("email", emailEdit.text.toString())

            editor.apply()

            Toast.makeText(this, "Profile Updated ✅", Toast.LENGTH_SHORT).show()

            finish() // go back
        }
    }
}