package mobin.shabanifar

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import mobin.shabanifar.models.*
import mobin.shabanifar.plugins.configureSerialization
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun main() {
    initDatabase()
    embeddedServer(Netty, port = 2003, host = "localhost", module = Application::module).start(wait = true)
}

fun Application.module() {
    configureSerialization()
    configureRouting()
}

fun initDatabase() {
    // Connect to the existing SQLite database file
    val dbPath = Thread.currentThread().contextClassLoader.getResource("naghmeh.db")?.path
    Database.connect(
        url = "jdbc:sqlite:$dbPath",
        driver = "org.sqlite.JDBC"
    )
    transaction {
        SchemaUtils.create(Poet) // Create the table if it doesn't exist
    }
}