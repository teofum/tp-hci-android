package com.example.tphci.ui.shareList


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tphci.ui.home.rememberWindowInfo
import com.example.tphci.ui.theme.TPHCITheme



// TODO uhh esto check en general
// está conectado con el menú de 3 puntitos o el icon de Share al acceder a los detalles de una lista



/**
 * Basic user model – adapt it to your backend DTO.
 */
data class User(
    val id: String,
    val fullName: String,
    val handle: String,
    val avatarRes: Int? = null, // local drawable, or null if you load from URL
)

/**
 * Main screen. Stateless: only UI + callbacks.
 */
@OptIn(ExperimentalMaterial3Api::class)//TODO: esto no ba
@Composable
fun ShareListScreen(
    selectedUsers: List<User>,
    suggestedUsers: List<User>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onUserToggle: (User) -> Unit,
    onRemoveSelectedUser: (User) -> Unit,
    onBackClick: () -> Unit,
    onDoneClick: () -> Unit,
) {

    val windowInfo = rememberWindowInfo()
    val maxWidth = windowInfo.maxWidth

    Scaffold(
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = onDoneClick,
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(52.dp),
                    shape = RoundedCornerShape(24.dp),
                ) {
                    Text("Listo", fontSize = 18.sp)
                }
            }
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = maxWidth)
                    .padding(16.dp)

            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar")
                    }

                    Text(
                        text = "Compartir lista",
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Spacer(modifier = Modifier.width(48.dp))
                }


            // Selected user “pill” on top
            if (selectedUsers.isNotEmpty()) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    items(selectedUsers) { user ->
                        SelectedUserChip(
                            user = user,
                            onRemove = { onRemoveSelectedUser(user) }
                        )
                    }
                }
            }

            // Search bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                placeholder = { Text("Buscar usuarios") },
                singleLine = true,
                shape = RoundedCornerShape(10.dp)
            )

            // Suggested users title
            Text(
                text = "Usuarios sugeridos",
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Suggested users list
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(suggestedUsers) { user ->
                    SuggestedUserRow(
                        user = user,
                        onClick = { onUserToggle(user) }
                    )
                }
            }
        }
            }
    }
}

/**
 * Pill with avatar, name, handle and close icon.
 */
@Composable
private fun SelectedUserChip(
    user: User,
    onRemove: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Avatar(user, size = 32.dp)

            Spacer(Modifier.width(8.dp))

            Column {
                Text(
                    text = user.fullName,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "@${user.handle}",
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }

            Spacer(Modifier.width(8.dp))

            IconButton(
                onClick = onRemove,
                modifier = Modifier.size(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Quitar usuario",
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

/**
 * Row in "Usuarios sugeridos".
 */
@Composable
private fun SuggestedUserRow(
    user: User,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Avatar(user, size = 44.dp)

        Spacer(Modifier.width(12.dp))

        Column {
            Text(
                text = user.fullName,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp
            )
            Text(
                text = "@${user.handle}",
                fontSize = 13.sp,
                color = Color.Gray
            )
        }
    }
}

/**
 * Simple avatar component.
 * Swap this to Coil/Glide if you load from URL.
 */
@Composable
private fun Avatar(
    user: User,
    size: Dp
) {
    if (user.avatarRes != null) {
        Image(
            painter = painterResource(id = user.avatarRes),
            contentDescription = user.fullName,
            modifier = Modifier
                .size(size)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
    } else {
        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = user.fullName.firstOrNull()?.uppercase() ?: "",
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/**
 * Simple preview with fake state, just to see the UI quickly.
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ShareListScreenPreview() {
    val selected = remember {
        mutableStateOf(
            listOf(
                User(
                    id = "1",
                    fullName = "Sophia Richards",
                    handle = "sophia.richards"
                )
            )
        )
    }

    val suggested = listOf(
        User("2", "Henry Clark", "henry.clark"),
        User("3", "Olivia Smith", "olivia.smith")
    )

    val search = remember { mutableStateOf("") }

    TPHCITheme {
        ShareListScreen(
            selectedUsers = selected.value,
            suggestedUsers = suggested,
            searchQuery = search.value,
            onSearchQueryChange = { search.value = it },
            onUserToggle = { user ->
                selected.value =
                    if (selected.value.any { it.id == user.id }) {
                        selected.value.filterNot { it.id == user.id }
                    } else {
                        selected.value + user
                    }
            },
            onRemoveSelectedUser = { user ->
                selected.value = selected.value.filterNot { it.id == user.id }
            },
            onBackClick = {},
            onDoneClick = {}
        )
    }
}
