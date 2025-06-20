package com.Neitirite
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import java.lang.System
import java.io.File
import java.nio.file.Files.deleteIfExists

val topicDirectory = File(System.getenv("TOPICS_PATH").toString())
class Topics {
    fun createTopic(topicName: String): String {
        val topicFile = File("${topicDirectory}/$topicName")
        if(!topicFile.exists()){
            topicFile.createNewFile()
            println("Created topic $topicName in ${topicFile.absolutePath}")
            return "Created topic \"$topicName\""
        } else {
            return "joined to the existing topic $topicName"
        }

    }

    fun deleteTopic(topicName: String?): String{
        val topicFile = File("${topicDirectory}/$topicName")
        if(topicFile.exists()){
            deleteIfExists(topicFile.toPath())
            println("Deleted topic $topicName in ${topicFile.absolutePath}")
            return "Deleted topic \"$topicName\""
        } else {
            return "Failed to delete $topicName: Topic does not exist"
        }
    }

    fun sendMessage(topicName: String, message: JsonObject): String{
        println(topicName)
        val topicFile = File("${topicDirectory}/$topicName")
        if(!topicFile.exists()){
            println("Failed to send message in $topicName. File does not exist: ${topicFile.absolutePath}")
            return "Failed to send message in $topicName: Topic does not exist"
        } else {
            val messages = topicFile.readText()
            println(messages)
            if(messages != "" && messages != "[]") {
                var parsedMessages = Json.decodeFromString<List<Serialization.Messages>>(messages)
                val newMessage = Serialization.Messages((parsedMessages.last().id.toInt() + 1).toString(), message)
                println("New messages: $newMessage")
                parsedMessages += newMessage
                println(Json.encodeToJsonElement(parsedMessages))
                topicFile.writeText(Json.encodeToJsonElement(parsedMessages).toString())
            } else {
                val newMessage = Serialization.Messages("0", message)
                val updatedMessages = Json.encodeToString(newMessage)
                topicFile.writeText("[$updatedMessages]")
            }
            return "Successfully sent message $message in $topicName"
        }
    }

    fun receiveMessage(topic: String): String{
        val topicFile = File("${topicDirectory}/$topic")
        val messages = topicFile.readText()
        if(messages != "" && messages != "[]"){
            val parsedMessages = Json.decodeFromString<MutableList<Serialization.Messages>>(messages)
            val message = parsedMessages.first().message
            parsedMessages.removeFirst()
            topicFile.writeText(Json.encodeToJsonElement(parsedMessages).toString())
            return Json.encodeToString(message)
        } else {
            return "There is no messages in topic"
        }


    }

}