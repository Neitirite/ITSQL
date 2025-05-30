package com.Neitirite
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlin.concurrent.thread

@Serializable
data class test(val username: String, val Group: String, val password: String)
fun main(){
    val test = test("sursname name", "group", "password")
    val json = Json.encodeToString(test)
    println(json)
    thread(start = true) {
        UserDBController().Register(json)
        UserDBController().SignIn(json)
        println("${Thread.currentThread().name} has run!")
    }

}
