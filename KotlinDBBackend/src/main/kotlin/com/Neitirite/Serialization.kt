package com.Neitirite

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

class Serialization {
    @Serializable
    data class ReceivedMessage(val id: String, val command : String, val properties: Map<String, JsonElement>)
    @Serializable
    data class UserInfo(val name: String, val surname: String, val group: String, val password: String)
    @Serializable
    data class MessageForSend(val id: String, val message: String)
}