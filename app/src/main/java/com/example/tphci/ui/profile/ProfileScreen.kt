package com.example.tphci.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tphci.MyApplication
import com.example.tphci.ui.home.HomeViewModel

@Composable
fun ProfileScreen(
    viewModel: HomeViewModel = viewModel(
        factory = HomeViewModel.provideFactory(
            (LocalContext.current.applicationContext as MyApplication).sessionManager,
            (LocalContext.current.applicationContext as MyApplication).userRepository,
            (LocalContext.current.applicationContext as MyApplication).shoppingRepository
        )
    )
) {

    val uiState = viewModel.uiState


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Perfil", style = MaterialTheme.typography.headlineMedium)



        if (!uiState.isAuthenticated) {
            Text("Debug login")
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                // TODO hardcodeado (hacerlo con form)
                // pueden ir a http://localhost:8080/docs con la API en Try it out
                // después ejecutar desde la página:
                // {
                //  "name": "test",
                //  "surname": "test",
                //  "email": "test@gmail.com",
                //  "password": "test1234",
                //  "metadata": {}
                // }
                // y después verificarlo
                viewModel.login("test@gmail.com", "test1234")
            }) {
                Text("hardcode login")
            }
        } else {


            Text("User login ok")
            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { viewModel.logout() }) {
                Text("Cerrar sesión")
            }

        }
    }
}