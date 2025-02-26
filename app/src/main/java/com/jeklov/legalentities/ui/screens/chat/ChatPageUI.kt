package com.jeklov.legalentities.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.jeklov.legalentities.data.db.MainDB
import com.jeklov.legalentities.data.models.Message
import com.jeklov.legalentities.data.models.User
import com.jeklov.legalentities.data.view.model.UserViewModel
import com.jeklov.legalentities.data.view.model.UserViewModelProvideFactory
import kotlinx.coroutines.launch

@Composable
fun ChatPageUI(
    dataBase: MainDB,
    paddingValues: PaddingValues,
    navigationController: NavHostController,
    snackbarHostState: SnackbarHostState,
    id: Int?
) {
    val viewModel: UserViewModel = viewModel(factory = UserViewModelProvideFactory(dataBase))
    val user = viewModel.getUserById(id)?.observeAsState()

    //if(user?.value?.messageList == null) user?.value?.messageList = listOf(Message("no message", false), Message("no message", false))
    val messages = user?.value?.messageList
    var newMessage by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    Column(
        Modifier
            .fillMaxSize()
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding()
            )
    ) {
        Divider()
        Text(text = if (user != null) user.value?.email + " " + user.value?.login + " " + id else "А выбрать?")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // list of message
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                if (messages != null) {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(messages) { message ->
                            MessageItem(message)
                        }
                    }
                } else {
                    Text(text = "Сообщений еще не было")
                }
            }

            // Поле для ввода нового сообщения
            Row(modifier = Modifier.fillMaxWidth()) {
                BasicTextField(
                    value = newMessage,
                    onValueChange = { newMessage = it },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                        .background(Color.White)
                )
                Button(onClick = {
                    if (newMessage.isNotBlank()) {
                        if (user != null) {
                            @Suppress("KotlinConstantConditions")
                            viewModel.setMessage(user.value!!, Message(newMessage, true))
                            newMessage = "" // clean message scope
                        } else {
                            scope.launch {
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
                    }
                }) {
                    Text("Отправить")
                }
            }
        }
    }
}

@Composable
fun MessageItem(message: Message) {
    val backgroundColor =
        if (message.isMine) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
    val textColor =
        if (message.isMine) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = if(message.isMine) 120.dp else 8.dp,
                end = if(!message.isMine) 120.dp else 8.dp,
                top = 8.dp,
                bottom = 8.dp
            )
            .background(backgroundColor)
            .padding(8.dp)
            .background(if (message.isMine) Color.Blue else Color.Red),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(text = message.text, color = textColor)
    }
}