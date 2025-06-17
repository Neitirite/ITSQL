package com.Neitirite
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import java.sql.Connection
import java.sql.DriverManager
import java.util.*
import kotlinx.coroutines.*
@Serializable
data class userInfo(val username: String, val Group: String, val password: String)

val db_url = "jdbc:postgresql://supernova-light.ru:5432/authorization"
val db_username = "Admin"
val db_password = "239w671290e12edasd!saf"
val props = mapOf("user" to db_username, "password" to db_password)


class UserDBController {
    fun Register(data: String) {
        val parsedData = Json.decodeFromString<userInfo>(data)
        val surname = parsedData.username.split(" ")[0]
        val name = parsedData.username.split(" ")[1]
        val group = parsedData.Group
        val password = parsedData.password
        println("parsed data is ${surname}, ${name}, ${group}, ${password}")

        val conn = DriverManager.getConnection(db_url, props.toProperties())
        val st = conn.createStatement()

        val getUsers = st.executeQuery("SELECT * FROM users WHERE surname = '${surname}' AND name = ${name} AND groupname = '${group}'")
        if(getUsers.next()) {
            println("пользователь уже существует")
        } else {
            val rs = st.executeQuery(
                "insert into users (surname, name, groupname, password) values ('${surname}', '${name}', '${group}', '${password}')")
            rs.close()
        }
        getUsers.close()
        st.close()
    }

    fun SignIn(data: String) {
        val parsedData = Json.decodeFromString<userInfo>(data)
        val surname = parsedData.username.split(" ")[0]
        val name = parsedData.username.split(" ")[1]
        val group = parsedData.Group
        val password = parsedData.password

        val conn = DriverManager.getConnection(db_url, props.toProperties())
        val st = conn.createStatement()
        val getUsers = st.executeQuery("SELECT * FROM users WHERE surname = '${surname}' AND name = ${name} AND groupname = '${group}' AND password = '${password}'")
        if(getUsers.next()) {
            println("Пользователь найден")
            TODO("Надо сделать ответ на фронт")
        } else {
            println("Пользователь не существует")
        }
    }
}
