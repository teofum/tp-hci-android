package com.example.tphci

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.tphci.ui.home.HomeScreen
import com.example.tphci.ui.theme.TPHCITheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TPHCITheme {
                HomeScreen()
            }
        }
    }
}