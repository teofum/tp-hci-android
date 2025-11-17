package com.example.tphci.ui.shareList


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tphci.R
import com.example.tphci.ui.home.rememberWindowInfo

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
                        text = stringResource(R.string.share_list),
                        fontWeight = FontWeight.SemiBold,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.close)
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
                    Text(stringResource(R.string.done), fontSize = 18.sp)
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
                        Icon(
                            Icons.Default.Close,
                            contentDescription = stringResource(R.string.close)
                        )
                    }

                    Text(
                        text = stringResource(R.string.share_list),
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Spacer(modifier = Modifier.width(48.dp))
                }


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
                    placeholder = { Text(stringResource(R.string.search_users)) },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp)
                )
            }
        }
    }
}

/**
 * Pill with avatar, name, handle and close icon.
 */
@Composable
fun SelectedShareUserChip(
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
                    text = ShareUser.email,
                    fontSize = 11.sp,
                )
            }

            Spacer(Modifier.width(8.dp))

            IconButton(
                onClick = onRemove,
                modifier = Modifier.size(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.close),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

/**
 * Simple avatar component.
 * Swap this to Coil/Glide if you load from URL.
 */
@Composable
fun Avatar(
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
