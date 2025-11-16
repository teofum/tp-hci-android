package com.example.tphci.ui.home


import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.platform.LocalContext

data class WindowInfo(
    val maxWidth: Dp,
    val useTwoColumns: Boolean
)

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun rememberWindowInfo(): WindowInfo {
    val activity = LocalContext.current as ComponentActivity
    val windowSize = calculateWindowSizeClass(activity)
    val maxWidth = when (windowSize.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 400.dp
        WindowWidthSizeClass.Medium -> 500.dp
        WindowWidthSizeClass.Expanded -> 700.dp
        else -> 400.dp
    }
    val useTwoColumns = windowSize.widthSizeClass == WindowWidthSizeClass.Expanded
    return WindowInfo(maxWidth, useTwoColumns)
}
