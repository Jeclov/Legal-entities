package com.jeklov.legalentities.ui.screens.menu

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import com.jeklov.legalentities.data.db.MainDB
import com.jeklov.legalentities.data.models.User
import kotlinx.coroutines.launch

@Composable
fun ActionButtonUI(
    snackbarHostState: SnackbarHostState,
    dataBase: MainDB,
    actionButtonState: MutableState<Boolean>
) {

    val scope = rememberCoroutineScope()

    if (actionButtonState.value) {
        ExtendedFloatingActionButton(
            text = { Text("Add new form") },
            icon = { Icon(Icons.Filled.Add, contentDescription = "") },
            onClick = {
                scope.launch {
                    dataBase.userDao().upsertUserItem(
                        User(
                            login = "rsfw",
                            email = "wrfe5f"
                        )
                    )
                    val result = snackbarHostState
                        .showSnackbar(
                            message = "Snackbar",
                            actionLabel = "Action",
                            duration = SnackbarDuration.Short
                        )
                    when (result) {
                        SnackbarResult.ActionPerformed -> {
                            /* Handle snackbar action performed */
                        }

                        SnackbarResult.Dismissed -> {
                            /* Handle snackbar dismissed */
                        }
                    }
                }
            }
        )
    }
}