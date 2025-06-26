package com.Neitirite
import kotlinx.serialization.json.*
import java.sql.DriverManager
import com.Neitirite.Serialization.*


val db_url = "jdbc:postgresql://supernova-light.ru:5432/authorization"
val db_username = "Admin"
val db_password = "239w671290e12edasd!saf"
val props = mapOf("user" to db_username, "password" to db_password)


class UserDBController {
    fun Register(data: String): String {
        val parsedData = Json.decodeFromString<UserInfo>(data)
        val surname = parsedData.surname
        val name = parsedData.name
        val group = parsedData.group
        val password = parsedData.password
        println("parsed data is $surname, $name, $group, $password")

        val conn = DriverManager.getConnection(db_url, props.toProperties())
        val st = conn.createStatement()

        val getUsers = st.executeQuery("SELECT * FROM users WHERE SURNAME = '${surname}' AND NAME = '${name}' AND GROUPNAME = '${group}'")
        if(getUsers.next()) {
            getUsers.close()
            st.close()
            println("User $surname $name from $group already exists")
            return "User $surname $name from $group already exists"
        } else {
            try {
                val rs = st.executeQuery(
                    "insert into users (SURNAME, NAME, GROUPNAME, PASSWORD) values ('${surname}', '${name}', '${group}', '${password}')"
                )
                rs.close()
            } catch (e: Exception) {
                println(e.message)
            }
            getUsers.close()
            st.close()
            println("Registered new user $surname $name from $group")
            return "User $surname $name from $group successfully registered"
        }


    }

    fun SignIn(data: String): String {
        val parsedData = Json.decodeFromString<UserInfo>(data)
        val surname = parsedData.surname
        val name = parsedData.name
        val group = parsedData.group
        val password = parsedData.password
        val conn = DriverManager.getConnection(db_url, props.toProperties())
        val st = conn.createStatement()
        val getUsers = st.executeQuery("SELECT * FROM users WHERE SURNAME = '${surname}' AND NAME = '${name}' AND GROUPNAME = '${group}' AND PASSWORD = '${password}'")
        if(getUsers.next()) {
            println("Found user $surname $name from $group")
            return "OK"
        } else {
            println("Can't find user $surname $name from $group")
            return "Incorrect login data"
        }
    }
}
