package com.clothy.clothyandroid.services



import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString


internal class EchoWebSocketListener(
    val output: (String) -> Unit,
    val ping: (String) -> Unit,
    val closing: () -> Unit
) : WebSocketListener() {
    override fun onOpen(webSocket: WebSocket, response: Response) {
        ping("Connected!")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {




        // Do something with the parameters

        output(text)
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        output("Receiving bytes : " + bytes.hex())
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        webSocket.close(NORMAL_CLOSURE_STATUS, null)
        ping("Closing : $code / $reason")
        closing()
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        ping("Error : " + t.message)
        closing()
    }

    companion object {
        const val NORMAL_CLOSURE_STATUS = 1000
    }
}