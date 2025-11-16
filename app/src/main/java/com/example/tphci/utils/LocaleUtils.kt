package com.example.tphci.utils

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

object LocaleUtils {
    
    fun setLocale(context: Context, languageCode: String): Context {
        val locale = when (languageCode) {
            "es" -> Locale("es")
            "en" -> Locale("en")
            else -> Locale.getDefault()
        }
        
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        
        return context.createConfigurationContext(config)
    }
    
    fun getCurrentLanguage(context: Context): String {
        return context.resources.configuration.locales[0].language
    }
}