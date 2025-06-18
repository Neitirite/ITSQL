package com.Neitirite
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlin.time.Duration.Companion.seconds

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
            webSocket ("/api"){
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
                                val command = text.split(" ")
                                if (command[0] == "joinTopic") {
                                    try {
                                        Topics().joinTopic(command[1])
                                    } catch (e: Exception) {
                                        println("Failed to join topic: ${e.message}")
                                    }

                                    break
                                }
                                else if (command[0] == "sendMessage") {

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
    }
}