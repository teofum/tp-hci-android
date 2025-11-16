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
    
    private val _theme = MutableStateFlow(getTheme())
    val theme: StateFlow<String> = _theme.asStateFlow()
    
    fun getLanguage(): String {
        return prefs.getString("language", "automatic") ?: "automatic"
    }
    
    fun setLanguage(language: String) {
        prefs.edit().putString("language", language).apply()
        _language.value = language
    }
    
    fun getTheme(): String {
        return prefs.getString("theme", "system") ?: "system"
    }
    
    fun setTheme(theme: String) {
        prefs.edit().putString("theme", theme).apply()
        _theme.value = theme
    }
}