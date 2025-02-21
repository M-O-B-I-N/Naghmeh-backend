package mobin.shabanifar

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import mobin.shabanifar.plugins.configureSerialization

fun main() {
    embeddedServer(Netty, port = 2003, host = "localhost", module = Application::module).start(wait = true)
}

fun Application.module() {
    configureSerialization()

}
