package com.Neitirite
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.Serializable
import kotlin.time.Duration.Companion.seconds
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonPrimitive


fun main() {
    embeddedServer(Netty, 5000, "0.0.0.0") {
        install(WebSockets){
            pingPeriod = 15.seconds
            timeout = 30.seconds
            maxFrameSize = Long.MAX_VALUE
            masking = false
        }
        println("Starting websocket api...")
        routing {
            webSocket ("/"){
                println("New connection: ${this.call.request.origin.remoteHost}")
                val binaryChunks = mutableListOf<ByteArray>()
                var id: String? = null
                var width: Int? = null
                var height: Int? = null
                try {
                    for (frame in incoming) {
                        when (frame) {
                            is Frame.Text -> {
                                val text = frame.readText()
                                val command = Json.decodeFromString<Serialization.Command>(text)
                                println("Received command: ${command.command}")
                                when (command.command) {
                                    "createTopic" -> {
                                        val topic = command.properties["name"]?.jsonPrimitive?.content
                                        try {
                                            Topics().createTopic(topic.toString())
                                        } catch (e: Exception) {
                                            println("Failed to create topic: ${e.message}")
                                        }
                                    }
                                    "sendMessage" -> {
                                        println(command.properties["topic"]?.jsonPrimitive?.content)
                                        println(command.properties["message"]?.jsonPrimitive?.content)
                                    }
                                }
                            }
                            else -> {
                                continue
                            }
                        }
                    }
                } catch (e: Exception) {
                    println("Error: ${e.message}")
                }
            }

        }
    }.start(wait = true)
}