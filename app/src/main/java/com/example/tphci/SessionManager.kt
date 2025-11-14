package com.example.tphci

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class SessionManager(context: Context) {
    private var preferences: SharedPreferences =
        context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun loadAuthToken(): String? {
        return preferences.getString(AUTH_TOKEN, null)
    }

    fun removeAuthToken() {
        preferences.edit {
            remove(AUTH_TOKEN)
        }
    }

    fun saveAuthToken(token: String) {
        preferences.edit {
            putString(AUTH_TOKEN, token)
        }
    }

    companion object {
        const val PREFERENCES_NAME = "my_app"
        const val AUTH_TOKEN = "auth_token"
    }
}