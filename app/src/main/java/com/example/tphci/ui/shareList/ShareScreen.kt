package com.example.tphci.ui.shareList


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tphci.MyApplication
import com.example.tphci.R
import com.example.tphci.ui.home.rememberWindowInfo


@Composable
fun ShareListRoute(
    listId: Int,
    onBackClick: () -> Unit
) {
    val application = LocalContext.current.applicationContext as MyApplication
    val viewModel: ShareListViewModel = viewModel(
        factory = ShareListViewModel.provideFactory(application, listId)
    )
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val context = LocalContext.current

    // Handle Error side effect
    val error = uiState.error
    LaunchedEffect(error) {
        if (error != null) {
            snackbarHostState.showSnackbar(
                message = error,
                actionLabel = context.getString(R.string.ok)
            )
            viewModel.clearError()
        }
    }

    ShareListScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onEmailInputChange = viewModel::onEmailInputChange,
        onAddEmail = viewModel::onAddEmail,
        onRemoveSharedUser = viewModel::onRemoveSharedUser,
        onBackClick = onBackClick
    )
}

@Composable
fun ShareListScreen(
    uiState: ShareListUiState,
    snackbarHostState: SnackbarHostState,
    onEmailInputChange: (String) -> Unit,
    onAddEmail: () -> Unit,
    onRemoveSharedUser: (ShareUser) -> Unit,
    onBackClick: () -> Unit
) {
    val windowInfo = rememberWindowInfo()
    val maxWidth = windowInfo.maxWidth
    val isTablet = maxWidth > 600.dp

    Dialog(
        onDismissRequest = onBackClick,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter)
            )

            Column(
                modifier = if (isTablet) {
                    Modifier
                        .widthIn(max = 600.dp)
                        .align(Alignment.Center)
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
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.share_list),
                        style = MaterialTheme.typography.titleLarge
                    )
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.close)
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = uiState.emailInput,
                        onValueChange = onEmailInputChange,
                        modifier = Modifier.weight(1f),
                        placeholder = { Text(stringResource(R.string.email)) },
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )
                    Button(
                        onClick = onAddEmail,
                        enabled = !uiState.isLoading && uiState.emailInput.isNotBlank(),
                        modifier = Modifier.height(56.dp)
                    ) {
                        Text(stringResource(R.string.add))
                    }
                }

                Text(
                    text = stringResource(R.string.shared_with),
                    style = MaterialTheme.typography.titleMedium
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    if (uiState.sharedUsers.isEmpty() && !uiState.isLoading) {
                        Text(
                            text = stringResource(R.string.no_shared_users),
                            modifier = Modifier.align(Alignment.Center),
                            color = Color.Gray
                        )
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(uiState.sharedUsers) { user ->
                                SharedUserRow(
                                    user = user,
                                    onRemove = { onRemoveSharedUser(user) }
                                )
                            }
                        }
                    }

                    if (uiState.isLoading) {
                        CircularProgressIndicator(Modifier.align(Alignment.Center))
                    }
                }
            }
        }
    }
}

data class ShareUser(
    val id: Int,
    val name: String,
    val surname: String,
    val email: String,
    val metadata: Unit,
    val createdAt: String,
    val updatedAt: String
)

val ShareUser.fullName: String
    get() = "$name $surname"

@Composable
private fun SharedUserRow(
    user: ShareUser,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
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
                    text = user.email,
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }
        }

        IconButton(onClick = onRemove) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(R.string.delete),
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}


@Composable
fun Avatar(
    user: ShareUser,
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
            text = user.fullName.firstOrNull()?.uppercase() ?: "",
            color = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.Bold
        )
    }
}