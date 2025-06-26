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
        println("parsed data is ${surname}, ${name}, ${group}, ${password}")

        val conn = DriverManager.getConnection(db_url, props.toProperties())
        val st = conn.createStatement()

        val getUsers = st.executeQuery("SELECT * FROM users WHERE surname = '${surname}' AND name = ${name} AND groupname = '${group}'")
        if(getUsers.next()) {
            getUsers.close()
            st.close()
            println("пользователь уже существует")
            return "User ${surname} ${name} from ${group} already exists"
        } else {
            try {
                val rs = st.executeQuery(
                    "insert into users (surname, name, groupname, password) values ('${surname}', '${name}', '${group}', '${password}')"
                )
                rs.close()
            } catch (e: Exception) {
                println(e.message)
            }
            getUsers.close()
            st.close()
            return "user ${surname} ${name} from ${group} successfully registered"
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
        val getUsers = st.executeQuery("SELECT * FROM users WHERE surname = '${surname}' AND name = ${name} AND groupname = '${group}' AND password = '${password}'")
        if(getUsers.next()) {
            println("Пользователь найден")
            TODO("Надо сделать ответ на фронт")
        } else {
            println("Пользователь не существует")
        }
        return ""
    }
}
