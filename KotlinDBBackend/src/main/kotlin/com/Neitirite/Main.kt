package com.Neitirite
import com.Neitirite.Serialization.MessageForSend
import com.Neitirite.Serialization.ReceivedMessage
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json


suspend fun main(){
    val host = "127.0.0.1"
    val port = 5000
    Logger().clearLogs()
    Logger().writeLog("Starting controller")
    val client = HttpClient(CIO) {
        install(WebSockets)
    }
    try {
        client.webSocket(
            method = HttpMethod.Get,
            host = host,
            port = port,
            path = "/"
        ) {
            Logger().writeLog("Connected to the message broker (ip: $host, port: $port)")
            send(Frame.Text("{\"command\": \"createTopic\", \"properties\":{\"name\": \"frontToBack\"}}"))
            var response = incoming.receive() as Frame.Text
            println(response.readText())
            Logger().writeLog(response.readText())
            send(Frame.Text("{\"command\": \"createTopic\", \"properties\":{\"name\": \"backToFront\"}}"))
            response = incoming.receive() as Frame.Text
            println(response.readText())
            Logger().writeLog(response.readText())
        }
        while (true) {
            client.webSocket(
                method = HttpMethod.Get,
                host = "127.0.0.1",
                port = 5000,
                path = "/"
            ) {
                send(Frame.Text("{\"command\": \"receiveMessage\", \"properties\":{\"topic\": \"frontToBack\"}}"))
                val response = incoming.receive() as Frame.Text
                if (response.readText() != "There is no messages in topic") {
                    val command = Json.decodeFromString<ReceivedMessage>(response.readText())
                    println(response.readText())
                    when (command.command) {
                        "register" -> {
                            println(command.properties)
                            val result = UserDBController().Register(Json.encodeToString(command.properties))
                            val message = MessageForSend(command.id, result)
                            send(
                                Frame.Text(
                                    "{\"command\": \"sendMessage\", \"properties\":{\"topic\": " +
                                            "\"backToFront\", \"message\":${Json.encodeToString(message)}}}"
                                )
                            )
                            val response = incoming.receive() as Frame.Text
                            println(response.readText())
                            Logger().writeLog(response.readText())
                        }

                        "login" -> {
                            val result = UserDBController().SignIn(Json.encodeToString(command.properties))
                            val message = MessageForSend(command.id, result)
                            send(
                                Frame.Text(
                                    "{\"command\": \"sendMessage\", \"properties\":{\"topic\": " +
                                            "\"backToFront\", \"message\":${Json.encodeToString(message)}}}"
                                )
                            )
                            val response = incoming.receive() as Frame.Text
                            println(response.readText())
                            Logger().writeLog(response.readText())
                        }
                    }
                }
            }
            delay(1000)

        }
    } catch (e: Exception) {Logger().writeLog("Error: ${e.message}")}

}
