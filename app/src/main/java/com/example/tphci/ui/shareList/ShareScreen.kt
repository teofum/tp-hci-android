@file:Suppress("UnusedImport")

package com.example.tphci.ui.share

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

data class Person(
  val id: String,
  val name: String,
  val handle: String
)

@Composable
fun ShareListScreen(
  me: Person,
  suggested: List<Person>,
  onClose: () -> Unit = {},
  onDone: (List<Person>) -> Unit = {}
) {
  var query by remember { mutableStateOf("") }
  var selected by remember { mutableStateOf(listOf(me)) } // “me” preselected as in the mock
  val filtered = remember(query, suggested) {
    if (query.isBlank()) suggested
    else suggested.filter {
      it.name.contains(query, true) || it.handle.contains(query, true)
    }
  }

  // Soft warm background like the mock
  val canvas = Color(0xFFF1EAE3)
  val darkText = Color(0xFF1E1E1E)
  val pillGreen = Color(0xFF29433A)

  Surface(color = Color(0xFF111111)) { // phone frame bg
    Surface(
      modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
      color = canvas,
      shape = RoundedCornerShape(22.dp),
      tonalElevation = 0.dp
    ) {
      Scaffold(
        containerColor = canvas,
        topBar = {
          SmallTopAppBar(
            title = {
              Text(
                "Compartir lista",
                style = MaterialTheme.typography.titleMedium.copy(
                  color = darkText,
                  fontWeight = FontWeight.SemiBold
                )
              )
            },
            navigationIcon = {
              IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = darkText)
              }
            },
            actions = { Spacer(Modifier.size(40.dp)) } // to mimic centered title spacing
          )
        },
        bottomBar = {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(20.dp),
            contentAlignment = Alignment.Center
          ) {
            Button(
              onClick = { onDone(selected) },
              colors = ButtonDefaults.buttonColors(containerColor = pillGreen),
              shape = RoundedCornerShape(16.dp),
              modifier = Modifier
                .height(48.dp)
                .widthIn(min = 180.dp)
            ) {
              Text("Listo")
            }
          }
        }
      ) { inner ->
        Column(
          Modifier
            .padding(inner)
            .padding(horizontal = 20.dp)
        ) {
          // Selected chips row (just the current user with a small clear)
          SelectedChipsRow(
            selected = selected,
            onRemove = { p -> if (p != me) selected = selected - p } // keep me unless you change logic
          )

          Spacer(Modifier.height(10.dp))

          // Search
          OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            singleLine = true,
            placeholder = { Text("Buscar usuarios") },
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = Color(0xFFBFC7BF),
              unfocusedBorderColor = Color(0xFFCFD5CF)
            ),
            modifier = Modifier
              .fillMaxWidth()
          )

          Spacer(Modifier.height(18.dp))

          Text(
            "Usuarios sugeridos",
            color = darkText,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
          )

          Spacer(Modifier.height(8.dp))

          LazyColumn(
            modifier = Modifier
              .fillMaxWidth()
              .weight(1f, fill = false)
          ) {
            items(filtered, key = { it.id }) { person ->
              UserRow(
                person = person,
                selected = selected.any { it.id == person.id },
                onToggle = {
                  selected =
                    if (selected.any { it.id == person.id }) selected - person
                    else selected + person
                }
              )
            }
          }
        }
      }
    }
  }
}

@Composable
private fun SelectedChipsRow(
  selected: List<Person>,
  onRemove: (Person) -> Unit
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier.padding(top = 6.dp, bottom = 6.dp)
  ) {
    selected.forEach { p ->
      Box(
        modifier = Modifier
          .padding(end = 12.dp)
          .wrapContentSize()
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White.copy(alpha = 0.9f))
            .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
          InitialsAvatar(name = p.name, size = 28.dp)
          Spacer(Modifier.width(8.dp))
          Column(modifier = Modifier.padding(end = 6.dp)) {
            Text(p.name, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            Text("@${p.handle}", fontSize = 10.sp, color = Color.Gray)
          }
        }
        // little close dot
        Box(
          modifier = Modifier
            .align(Alignment.TopEnd)
            .offset(x = 6.dp, y = (-6).dp)
            .size(18.dp)
            .clip(CircleShape)
            .background(Color(0xFFFFD6DB))
            .clickable { onRemove(p) },
          contentAlignment = Alignment.Center
        ) {
          Text("×", fontWeight = FontWeight.Black, fontSize = 12.sp, color = Color(0xFFB21F2D))
        }
      }
    }
  }
}

@Composable
private fun UserRow(
  person: Person,
  selected: Boolean,
  onToggle: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onToggle() }
      .padding(vertical = 10.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    InitialsAvatar(name = person.name)
    Spacer(Modifier.width(12.dp))
    Column(Modifier.weight(1f)) {
      Text(
        person.name,
        fontSize = 15.sp,
        fontWeight = FontWeight.SemiBold,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )
      Text("@${person.handle}", fontSize = 12.sp, color = Color.Gray)
    }
    if (selected) {
      AssistChip(
        onClick = onToggle,
        label = { Text("Agregado") }
      )
    }
  }
}

@Composable
private fun InitialsAvatar(name: String, size: androidx.compose.ui.unit.Dp = 44.dp) {
  val initials = name.split(" ")
    .filter { it.isNotBlank() }
    .take(2)
    .joinToString("") { it.first().uppercaseChar().toString() }

  // deterministic pastel per name
  val seed = name.hashCode()
  val rnd = Random(seed)
  val color = Color(0xFFAAAAAA).copy(
    red = (0.6f + rnd.nextFloat() * 0.3f),
    green = (0.6f + rnd.nextFloat() * 0.3f),
    blue = (0.6f + rnd.nextFloat() * 0.3f)
  )

  Box(
    modifier = Modifier
      .size(size)
      .clip(CircleShape)
      .background(color),
    contentAlignment = Alignment.Center
  ) {
    Text(initials, color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 14.sp)
  }
}

@Preview(showBackground = true, backgroundColor = 0xFF101010)
@Composable
private fun ShareListPreview() {
  val me = Person("me", "Sophia Richards", "sophia.richards")
  val suggested = listOf(
    Person("1", "Henry Clark", "henry.clark"),
    Person("2", "Olivia Smith", "olivia.smith"),
    Person("3", "Mariana López", "mariana.lo"),
    Person("4", "Wei Chen", "weichen"),
  )
  ShareListScreen(me = me, suggested = suggested)
}
