package com.jeklov.legalentities.ui.screens.authorization

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupUI(
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

    val signupData by viewModel.signupData.observeAsState(null)

    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailState by remember { mutableStateOf("") }
    val isEmailValid = remember { mutableStateOf(true) }

    val errorIcon: @Composable (() -> Unit) = {
        Icon(Icons.Rounded.Edit, "Error")
    }

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
            Text(stringResource(R.string.signup))

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

            OutlinedTextField(
                value = emailState,
                onValueChange = {
                    emailState = it
                    isEmailValid.value = isValidEmail(it)
                },
                label = {
                    Text(text = "Email")
                },
                trailingIcon = if (!isEmailValid.value) errorIcon else null,
                isError = !isEmailValid.value
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (isEmailValid.value) {
                        viewModel.signup(
                            login = login,
                            password = password,
                            email = emailState
                        )
                    }
                }
            ) {
                Text(text = stringResource(R.string.login))
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(
                onClick = {
                    navigationController.navigate(Screens.Login.screen) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            ) {
                Text(text = "I already have an account, Login")
            }

            Spacer(modifier = Modifier.height(8.dp))

            when (signupData) {
                is Resource.Success<*> -> {
                    val userData = (signupData as Resource.Success).data
                    Text("Signup successful!")
                    Text(text = userData.role)
                }

                is Resource.Error -> {
                    val errorMessage = (signupData as Resource.Error).exception
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

fun isValidEmail(email: String): Boolean {
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
    return emailRegex.matches(email)
}