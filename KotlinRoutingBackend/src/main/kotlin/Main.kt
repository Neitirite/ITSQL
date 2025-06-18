import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun main(){
    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
        routing {
            get("/hello") {
                call.respondText("Hello")
            }
        }
    }.start(wait = true)
}