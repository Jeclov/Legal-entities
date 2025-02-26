package com.jeklov.legalentities.ui.screens.authorization

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.jeklov.legalentities.MainActivity
import com.jeklov.legalentities.R
import com.jeklov.legalentities.data.repository.AuthorizationRepository
import com.jeklov.legalentities.data.util.Resource
import com.jeklov.legalentities.data.view.model.AuthorizationViewModel
import com.jeklov.legalentities.data.view.model.AuthorizationViewModelProviderFactory
import com.jeklov.legalentities.ui.screens.Screens
import androidx.compose.runtime.livedata.observeAsState

@Composable
fun LoginUI(
    paddingValues: PaddingValues,
    navigationController: NavHostController,
    context: MainActivity,
    application: Application
) {
    val viewModel: AuthorizationViewModel = viewModel(
        factory = AuthorizationViewModelProviderFactory(
            application,
            AuthorizationRepository()
        )
    )
    val loginData by viewModel.loginToken.observeAsState(null)

    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        Modifier
            .fillMaxSize()
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding()
            )
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.primary
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(stringResource(R.string.login))

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = login,
                onValueChange = {
                    login = it
                },
                label = {
                    Text(text = "Login")
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                },
                label = {
                    Text(text = "Password")
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    viewModel.login(
                        login = login,
                        password = password
                    )
                }
            ) {
                Text(text = stringResource(R.string.login))
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(
                onClick = {
                    navigationController.navigate(Screens.Signup.screen) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            ) {
                Text(text = "Don't have an account, Signup")
            }

            Spacer(modifier = Modifier.height(4.dp))

            TextButton(
                onClick = {
                    navigationController.navigate(Screens.Chat.screen) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            ) {
                Text(text = "Chat")
            }

            Spacer(modifier = Modifier.height(4.dp))

            when (loginData) {
                is Resource.Success<*> -> {
                    val userData = (loginData as Resource.Success).data
                    Text("Signup successful!")
                    Text(text = userData.role)
                }

                is Resource.Error -> {
                    val errorMessage = (loginData as Resource.Error).exception
                    Text("Error: " + errorMessage)
                }

                is Resource.Loading -> {
                    Text("Loading")
                }

                null -> {
                    Text("null")
                }
            }
        }
    }
}