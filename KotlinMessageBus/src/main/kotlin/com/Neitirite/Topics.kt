package com.Neitirite
import java.lang.System
import java.io.File
import java.nio.file.Files.deleteIfExists

var isTopicFileBusy = false
val topicDirectory = File(System.getenv("TOPICS_PATH").toString())
class Topics {
    fun createTopic(topicName: String): String {
        val topicFile = File("${topicDirectory}/$topicName")
        if(!topicFile.exists()){
            topicFile.createNewFile()
            println("Created topic $topicName in ${topicFile.absolutePath}")
            return "Created topic $topicName"
        } else {
            return "joined to the existing topic $topicName"
        }

    }

    fun deleteTopic(topicName: String): String{
        val topicFile = File("${topicDirectory}/$topicName")
        deleteIfExists(topicFile.toPath())
        if(topicFile.exists()){
            println("Deleted topic $topicName in ${topicFile.absolutePath}")
            return "Deleted topic $topicName"
        } else {
            return "Failed to delete $topicName: Topic does not exist"
        }
    }

    fun sendMessage(topicName: String, message: String): String{
        val topicFile = File("${topicDirectory}/$topicName")
        if(!topicFile.exists()){
            println("Failed to send message in $topicName. File does not exist: ${topicFile.absolutePath}")
            return "Failed to send message in $topicName: Topic does not exist"
        } else {
            if(!isTopicFileBusy){
                isTopicFileBusy = true

            } else {

            }
            return "Successfully sent message $message in $topicName"
        }
    }
}