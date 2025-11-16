package com.example.tphci.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun EmojiPicker(
    onSelect: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val emojis = listOf("📦","🍞","🥩","🍪","🧀","🍰","🍎","✨","⭐","🥛","🥬","🥫","🍝","🍚","🧻","🧼","🧃","🍗","🐟","🧂")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { onDismiss() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Seleccionar emoji", style = MaterialTheme.typography.titleMedium)

            Spacer(Modifier.height(12.dp))

            androidx.compose.foundation.lazy.grid.LazyVerticalGrid(
                columns = androidx.compose.foundation.lazy.grid.GridCells.Fixed(5),
                modifier = Modifier.height(200.dp)
            ) {
                items(emojis.size) { i ->
                    Box(
                        modifier = Modifier
                            .padding(6.dp)
                            .size(40.dp)
                            .background(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                RoundedCornerShape(8.dp)
                            )
                            .clickable { onSelect(emojis[i]) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(emojis[i], fontSize = MaterialTheme.typography.headlineSmall.fontSize)
                    }
                }
            }
        }
    }
}
