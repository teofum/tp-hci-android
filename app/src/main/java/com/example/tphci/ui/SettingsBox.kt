package com.example.tphci.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.activity.ComponentActivity
import com.example.tphci.MyApplication
import com.example.tphci.R
import com.example.tphci.ui.home.rememberWindowInfo
import com.example.tphci.utils.LocaleUtils

@Composable
fun SettingsBox(
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val settingsRepository = (context.applicationContext as MyApplication).settingsRepository
    val currentLanguage by settingsRepository.language.collectAsState()
    val currentTheme by settingsRepository.theme.collectAsState()
    var selectedLanguage by remember { mutableStateOf(currentLanguage) }
    var selectedTheme by remember { mutableStateOf(currentTheme) }
    
    LaunchedEffect(currentLanguage) {
        selectedLanguage = currentLanguage
    }
    
    LaunchedEffect(currentTheme) {
        selectedTheme = currentTheme
    }
    
    val languageOptions = listOf(
        "automatic" to stringResource(R.string.automatic),
        "es" to stringResource(R.string.spanish),
        "en" to stringResource(R.string.english)
    )
    
    val themeOptions = listOf(
        "system" to stringResource(R.string.system),
        "light" to stringResource(R.string.light),
        "dark" to stringResource(R.string.dark)
    )

    val windowInfo = rememberWindowInfo()
    val maxWidth = windowInfo.maxWidth
    val isTablet = maxWidth > 600.dp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = if (isTablet) {
                Modifier
                    .widthIn(max = 600.dp)
                    .heightIn(max = 400.dp)
                    .background(MaterialTheme.colorScheme.background, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            } else {
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp)
            },
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.settings),
                        style = MaterialTheme.typography.titleLarge
                    )
                    IconButton(onClick = onClose) {
                        Icon(Icons.Default.Close, contentDescription = stringResource(R.string.close))
                    }
                }

                Text(
                    text = stringResource(R.string.language),
                    style = MaterialTheme.typography.titleMedium
                )
                
                languageOptions.forEach { (value, label) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = selectedLanguage == value,
                                onClick = { 
                                    selectedLanguage = value
                                    settingsRepository.setLanguage(value)
                                    (context as? ComponentActivity)?.recreate()
                                }
                            )
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedLanguage == value,
                            onClick = { 
                                selectedLanguage = value
                                settingsRepository.setLanguage(value)
                                (context as? ComponentActivity)?.recreate()
                            }
                        )
                        Text(
                            text = label,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }

                Text(
                    text = stringResource(R.string.theme),
                    style = MaterialTheme.typography.titleMedium
                )
                
                themeOptions.forEach { (value, label) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = selectedTheme == value,
                                onClick = { 
                                    selectedTheme = value
                                    settingsRepository.setTheme(value)
                                }
                            )
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedTheme == value,
                            onClick = { 
                                selectedTheme = value
                                settingsRepository.setTheme(value)
                            }
                        )
                        Text(
                            text = label,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }

                if (isTablet) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = onClose) { 
                            Text(stringResource(R.string.close)) 
                        }
                    }
                }
        }
    }
}