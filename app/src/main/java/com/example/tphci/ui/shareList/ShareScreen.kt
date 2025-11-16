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

/**
 * Basic ShareUser model – adapt it to your backend DTO.
 */
data class ShareUser(
    val id: Int,
    val name: String,
    val surname: String,
    val email: String,
    val metadata: Unit,
    val createdAt: String,
    val updatedAt: String
)

// Helper property to minimize changes in composables
private val ShareUser.fullName: String
    get() = "$name $surname"

/**
 * Main screen. Stateless: only UI + callbacks.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShareListScreen(
    selectedShareUsers: List<ShareUser>,
    suggestedShareUsers: List<ShareUser>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onShareUserToggle: (ShareUser) -> Unit,
    onRemoveSelectedShareUser: (ShareUser) -> Unit,
    onBackClick: () -> Unit,
    onDoneClick: () -> Unit,
) {

    val windowInfo = rememberWindowInfo()
    val maxWidth = windowInfo.maxWidth

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Compartir lista",
                        fontWeight = FontWeight.SemiBold,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar"
                        )
                    }
                }
            )
        },
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
            // Selected ShareUser “pill” on top
            if (selectedShareUsers.isNotEmpty()) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    items(selectedShareUsers) { ShareUser ->
                        SelectedShareUserChip(
                            ShareUser = ShareUser,
                            onRemove = { onRemoveSelectedShareUser(ShareUser) }
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

            // Suggested ShareUsers title
            Text(
                text = "Usuarios sugeridos",
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Suggested ShareUsers list
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(suggestedShareUsers) { ShareUser ->
                    SuggestedShareUserRow(
                        ShareUser = ShareUser,
                        onClick = { onShareUserToggle(ShareUser) }
                    )
                }
            }
        }
    }
}

/**
 * Pill with avatar, name, handle and close icon.
 */
@Composable
private fun SelectedShareUserChip(
    ShareUser: ShareUser,
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
            Avatar(ShareUser, size = 32.dp)

            Spacer(Modifier.width(8.dp))

            Column {
                Text(
                    text = ShareUser.fullName,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = ShareUser.email, // Changed from @${ShareUser.handle}
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
private fun SuggestedShareUserRow(
    ShareUser: ShareUser,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Avatar(ShareUser, size = 44.dp)

        Spacer(Modifier.width(12.dp))

        Column {
            Text(
                text = ShareUser.fullName,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp
            )
            Text(
                text = ShareUser.email, // Changed from @${ShareUser.handle}
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
    ShareUser: ShareUser,
    size: Dp
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = ShareUser.fullName.firstOrNull()?.uppercase() ?: "",
            color = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * Simple preview with fake state, just to see the UI quickly.
 */
@Preview(showBackground = true, showSystemUi = true) // TODO : borrar el preview (testing)
@Composable
private fun ShareListScreenPreview() {
    val selected = remember {
        mutableStateOf(
            listOf(
                ShareUser(
                    id = 1,
                    name = "Sophia",
                    surname = "Richards",
                    email = "sophia.richards@example.com",
                    metadata = Unit,
                    createdAt = "2025-01-01",
                    updatedAt = "2025-01-01"
                )
            )
        )
    }

    val suggested = listOf(
        ShareUser(
            id = 2, name = "Henry", surname = "Clark",
            email = "henry.clark@example.com", metadata = Unit,
            createdAt = "2025-01-01", updatedAt = "2025-01-01"
        ),
        ShareUser(
            id = 3, name = "Olivia", surname = "Smith",
            email = "olivia.smith@example.com", metadata = Unit,
            createdAt = "2025-01-01", updatedAt = "2025-01-01"
        )
    )

    val search = remember { mutableStateOf("") }

    TPHCITheme {
        ShareListScreen(
            selectedShareUsers = selected.value,
            suggestedShareUsers = suggested,
            searchQuery = search.value,
            onSearchQueryChange = { search.value = it },
            onShareUserToggle = { ShareUser ->
                selected.value =
                    if (selected.value.any { it.id == ShareUser.id }) {
                        selected.value.filterNot { it.id == ShareUser.id }
                    } else {
                        selected.value + ShareUser
                    }
            },
            onRemoveSelectedShareUser = { ShareUser ->
                selected.value = selected.value.filterNot { it.id == ShareUser.id }
            },
            onBackClick = {},
            onDoneClick = {}
        )
    }
}
