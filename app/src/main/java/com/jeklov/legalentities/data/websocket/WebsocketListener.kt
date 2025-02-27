//package com.jeklov.legalentities.data.websocket
//
//import com.jeklov.legalentities.data.models.Message
//import com.jeklov.legalentities.data.view.model.WebsocketViewModel
//import okhttp3.Response
//import okhttp3.WebSocket
//import okhttp3.WebSocketListener
//
//class WebsocketListener(
//    private val viewModel: WebsocketViewModel
//) : WebSocketListener() {
//
//    override fun onOpen(webSocket: WebSocket, response: Response) {
//        super.onOpen(webSocket, response)
//        viewModel.setStatus(true)
//        webSocket.send("Android device connected")
//    }
//
//    override fun onMessage(webSocket: WebSocket, text: String) {
//        super.onMessage(webSocket, text)
//        viewModel.setMessage(Message(text, isMine = false))
//    }
//
//    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
//        super.onClosing(webSocket, code, reason)
//    }
//
//    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
//        super.onClosed(webSocket, code, reason)
//        viewModel.setStatus(false)
//    }
//
//    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
//        super.onFailure(webSocket, t, response)
//    }
//}