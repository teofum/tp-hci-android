package com.example.tphci.ui.shareList

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tphci.data.network.RetrofitClient
import kotlinx.coroutines.launch
import com.example.tphci.data.model.User

class ShareListViewModel : ViewModel() {

    var suggestedUsers by mutableStateOf<List<User>>(emptyList())
        private set

    init {
        loadUsers()
    }

    private fun loadUsers() {
        viewModelScope.launch {
            suggestedUsers = RetrofitClient.api.getUsers()
        }
    }
}
