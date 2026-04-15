package com.example.sokogarden

import android.content.Context

class SessionManager(context: Context) {

    private val prefs = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    fun login() {
        prefs.edit().putBoolean("isLoggedIn", true).apply()
    }

    fun logout() {
        prefs.edit().putBoolean("isLoggedIn", false).apply()
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean("isLoggedIn", false)
    }
}