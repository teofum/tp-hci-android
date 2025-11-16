package com.example.tphci.data.repository

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    
    private val _language = MutableStateFlow(getLanguage())
    val language: StateFlow<String> = _language.asStateFlow()
    
    fun getLanguage(): String {
        return prefs.getString("language", "automatic") ?: "automatic"
    }
    
    fun setLanguage(language: String) {
        prefs.edit().putString("language", language).apply()
        _language.value = language
    }
}