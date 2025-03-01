package mobin.shabanifar

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import mobin.shabanifar.models.*
import mobin.shabanifar.models.favorite.Favorite
import mobin.shabanifar.plugins.configureSerialization
import mobin.shabanifar.repository.FavoriteRepository
import mobin.shabanifar.routes.favoriteRoutes
import mobin.shabanifar.service.FavoriteService
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun main() {
    initDatabase()
    embeddedServer(Netty, port = 2003, host = "localhost", module = Application::module).start(wait = true)
}

fun Application.module() {
    configureSerialization()
    val favoriteRepository = FavoriteRepository()
    val favoriteService = FavoriteService(favoriteRepository)

    routing {
        createRoute()
        favoriteRoutes(favoriteService)
    }
}

fun initDatabase() {
    // Connect to the existing SQLite database file
    Database.connect(
        url = "jdbc:sqlite:src/main/kotlin/data/naghmeh.db",
        driver = "org.sqlite.JDBC"
    )
    transaction {
        SchemaUtils.create(Poet, Poem, Cat, Verse, PoetImage, Favorite) // Create the table if it doesn't exist
    }
}