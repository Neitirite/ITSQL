package com.Neitirite

import com.google.gson.Gson
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File

@Serializable
data class Props(val width: Int, val height: Int, val id: String)

@Serializable
data class Command(val Info: Props)

class Parser {
    fun parseMessage(data: String): Command {
        val parsedData = Json.decodeFromString<Command>(data)
        println("Width: ${parsedData.Info.width}")
        println("Height: ${parsedData.Info.height}")
        println("ID: ${parsedData.Info.id}")
        return parsedData
    }
}