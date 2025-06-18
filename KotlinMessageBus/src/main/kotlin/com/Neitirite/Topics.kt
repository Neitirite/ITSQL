package com.Neitirite
import java.lang.System
import java.io.File
class Topics {
    fun createTopic(topicName: String) {
        val topicDirectory = File(System.getenv("TOPICS_PATH").toString())
        val topicFile = File("${topicDirectory}/$topicName")
        if(!topicFile.exists()){
            topicFile.createNewFile()
        }
        println("Created topic $topicName in $topicFile")
    }
}