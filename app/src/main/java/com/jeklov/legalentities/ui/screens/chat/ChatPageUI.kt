package com.jeklov.legalentities.ui.screens.chat

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.TextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.jeklov.legalentities.MainActivity
import com.jeklov.legalentities.data.db.MainDB
import com.jeklov.legalentities.data.models.Message
import com.jeklov.legalentities.data.models.User
import com.jeklov.legalentities.data.models.WebsocketRequest
import com.jeklov.legalentities.data.repository.WebsocketRepository
import com.jeklov.legalentities.data.util.Constants.Companion.BASE_URL_WEBSOCKET
import com.jeklov.legalentities.data.util.Resource
import com.jeklov.legalentities.data.view.model.WebsocketViewModel
import com.jeklov.legalentities.data.view.model.WebsocketViewModelProviderFactory
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.util.concurrent.TimeUnit

var userGlobal: User? = null

@Composable
fun ChatPageUI(
    dataBase: MainDB,
    paddingValues: PaddingValues,
    navigationController: NavHostController,
    snackbarHostState: SnackbarHostState,
    context: MainActivity,
    application: Application
) {
//    val viewModelDataBase: UserViewModel =
//        viewModel(factory = UserViewModelProviderFactory(dataBase))
//
//    val viewModelWebsocket: WebsocketViewModel = viewModel()
//    val webSocketListener: WebSocketListener = WebsocketListener(viewModelWebsocket)
//    val okHttpClient = OkHttpClient()
//    var websocket by remember { mutableStateOf<WebSocket?>(null) }
//
//    val connectionToServer = viewModelWebsocket.socketStatus.observeAsState()
//
//    viewModelWebsocket.messageBody.observe(context, Observer {
//        viewModelDataBase.setMessage(userGlobal!!, it)
//    })
//
//
//    //if(user?.value?.messageList == null) user?.value?.messageList = listOf(Message("no message", false), Message("no message", false))
//    val messages = userGlobal?.messageList
//    var newMessage by remember { mutableStateOf("") }
//
//    Column(
//        Modifier
//            .fillMaxSize()
//            .padding(
//                top = paddingValues.calculateTopPadding(),
//                bottom = paddingValues.calculateBottomPadding()
//            )
//    ) {
//        HorizontalDivider()
//        Text(text = userGlobal?.email + " " + userGlobal?.login + " " + userGlobal?.id)
//        Text(text = if (connectionToServer.value == true) "Подключено" else "Не подключено")
//        Button(
//            onClick = {
//                websocket = okHttpClient.newWebSocket(createRequest(), webSocketListener)
//            }
//        ) {
//            Text(text = "Подключиться")
//        }
//        Button(
//            onClick = {
//                websocket?.close(1000, "Cancelled Manually from Android ;)")
//            }
//        ) {
//            Text(text = "Отключиться")
//        }
//
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp)
//        ) {
//            // list of message
//            Box(
//                modifier = Modifier.weight(1f),
//                contentAlignment = Alignment.Center,
//            ) {
//                if (messages != null) {
//                    //Text(text = textD)
//                    LazyColumn(modifier = Modifier.fillMaxSize()) {
//                        items(messages) { message ->
//                            MessageItem(message)
//                        }
//                    }
//                } else {
//                    Text(text = "Сообщений еще не было")
//                }
//            }
//
//            // Поле для ввода нового сообщения
//            Row(modifier = Modifier.fillMaxWidth()) {
//                BasicTextField(
//                    value = newMessage,
//                    onValueChange = { newMessage = it },
//                    modifier = Modifier
//                        .weight(1f)
//                        .padding(end = 8.dp)
//                        .background(Color.White)
//                )
//                Button(onClick = {
//                    if (newMessage.isNotBlank()) {
//                        websocket?.send(newMessage)
//                        viewModelWebsocket.setMessage(Message(newMessage, true))
//                        newMessage = "" // clean message scope
//                    }
//                }) {
//                    Text("Отправить")
//                }
//            }
//        }
//    }
    /*} else {
        Box(
            Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                )
        ) {
            Text(text = "Выбрать чат!")
        }
    }*/

//    val websocketRepository = WebsocketRepository()

//    val viewModelWebSocket: WebsocketViewModel =
//        viewModel(
//            factory = WebsocketViewModelProviderFactory(
//                application = application,
//                repository = websocketRepository
//            )
//        )

    var messages by remember { mutableStateOf(listOf<Message>()) }
    var websocket: WebSocket? by remember { mutableStateOf(null) }
    val client = remember {
        OkHttpClient.Builder()
            .readTimeout(0, TimeUnit.MILLISECONDS)
            .build()
    }


    //val websocketData = viewModelWebSocket.websocketData.observeAsState()


    // Создание WebSocket при первом запуске
    LaunchedEffect(Unit) {
        val request = Request.Builder()
                .url("ws://185.239.50.182:10000/ws/chat/1/")
                .build()

        websocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                // Соединение открыто
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                // Обработка полученного сообщения
                messages = messages + Message(text, isMine = false) // false - чужое сообщение
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                // Обработка ошибки
            }
        })
    }

    // UI для отображения сообщений и ввода нового сообщения
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("WebSocket Messages:", style = MaterialTheme.typography.titleLarge)

        // Отображение списка сообщений
        LazyColumn {
            items(messages) { message ->
                MessageItem(message)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        var newMessage by remember { mutableStateOf("") }

        // Поле для ввода нового сообщения
        TextField(
            value = newMessage,
            onValueChange = { newMessage = it },
            label = { Text("Enter message") }
        )

        Button(onClick = {
            // Создание нового сообщения и добавление его в список
            val messageToSend = Message(newMessage, isMine = true) // true - ваше сообщение
            messages = messages + messageToSend // Добавляем сообщение в список
            websocket?.send(newMessage) // Отправка сообщения через WebSocket
            newMessage = "" // Очистка поля ввода
        }) {
            Text("Send")
        }
    }

    // Закрытие WebSocket при уничтожении компонента
    DisposableEffect(Unit) {
        onDispose {
            websocket?.send("Closing")
            websocket?.close(1000, "Closing") // Закрываем соединение
        }
    }
}

//private fun createRequest(
//    viewModelWebSocket: WebsocketViewModel,
//    websocketData: State<Resource<WebsocketRequest>?>
//): Request? {
//    val res = viewModelWebSocket.getWebsocketData(userGlobal!!.login)
//    when(res) {
//        is Resource.Success<WebsocketRequest> -> {
//            val websocketURL = BASE_URL_WEBSOCKET + websocketData
//            return Request.Builder()
//                .url(websocketURL)
//                .build()
//        }
//
//        is Resource.Error -> {
//            return null
//        }
//
//        is Resource.Loading -> {
//            return null
//        }
//
//        null -> {
//            return null
//        }
//    }
//}

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
                start = if (message.isMine) 120.dp else 8.dp,
                end = if (!message.isMine) 120.dp else 8.dp,
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