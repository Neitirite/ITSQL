package com.Neitirite
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlin.concurrent.thread
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.delay
import com.Neitirite.Serialization.*

suspend fun main(){
    val client = HttpClient(CIO) {
        install(WebSockets)
    }
    client.webSocket(
        method = HttpMethod.Get,
        host = "127.0.0.1",
        port = 5000,
        path = "/"
    ) {
        send(Frame.Text("{\"command\": \"createTopic\", \"properties\":{\"name\": \"frontToBack\"}}"))
        var response = incoming.receive() as Frame.Text
        println(response.readText())
        send(Frame.Text("{\"command\": \"createTopic\", \"properties\":{\"name\": \"backToFront\"}}"))
        response = incoming.receive() as Frame.Text
        println(response.readText())
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
                        send(Frame.Text("{\"command\": \"sendMessage\", \"properties\":{\"topic\": " +
                                "\"backToFront\", \"message\":${Json.encodeToString(message)}}}"))
                        val response = incoming.receive() as Frame.Text
                        println(response.readText())
                    }
                    "login" -> {
                        val result = UserDBController().SignIn(Json.encodeToString(command.properties))
                        val message = MessageForSend(command.id, result)
                        send(Frame.Text("{\"command\": \"sendMessage\", \"properties\":{\"topic\": " +
                                "\"backToFront\", \"message\":${Json.encodeToString(message)}}}"))
                        val response = incoming.receive() as Frame.Text
                        println(response.readText())
                    }
                }
            }
        }
        delay(1000)
    }

}
