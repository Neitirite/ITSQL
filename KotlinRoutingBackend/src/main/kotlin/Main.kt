import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.date.*
import java.io.File

val STATIC_PATH = System.getenv("STATIC_PATH") ?: "~/Документы/IntelliJ/ITSQL/static/"
val TEMPLATES_PATH = System.getenv("TEMPLATES_PATH") ?: "~/Документы/IntelliJ/ITSQL/templates/"

fun main() {
    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
        routing {

            get("/hello") {
                call.respondText("Hello")
            }

            get("/") {
                val cookies = call.request.cookies

                if (cookies != null && (cookies.get("student_id") != null || cookies.get("student_id") != "")) {
                    call.respondFile(File(TEMPLATES_PATH + "task.html"))

                } else {
                    call.respondRedirect("/sign_in")
                }

                TODO("Проверяка, закончил он тестирование или нет")
            }

            get("/register") {
                call.respondFile(File(TEMPLATES_PATH + "register.html"))
            }

            get("/login") {
                call.respondFile(File(TEMPLATES_PATH + "sign_in.html"))
            }

            get("/logout") {
                val cookie = call.response.cookies
                cookie.append("student_id", "", expires = GMTDate(0))
            }

            get("/static/{fileName}") {
                call.respondFile(File(STATIC_PATH + call.parameters["fileName"]))
            }
        }
    }.start(wait = true)
}