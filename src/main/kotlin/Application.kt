package mobin.shabanifar

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import mobin.shabanifar.models.*
import mobin.shabanifar.models.favorite.Favorite
import mobin.shabanifar.models.poet.Poet
import mobin.shabanifar.plugins.configureSerialization
import mobin.shabanifar.repository.FavoriteRepository
import mobin.shabanifar.repository.PoetRepository
import mobin.shabanifar.routes.favoriteRoutes
import mobin.shabanifar.routes.poetRoutes
import mobin.shabanifar.service.FavoriteService
import mobin.shabanifar.service.PoetService
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
    val poetRepository = PoetRepository()
    val poetService = PoetService(poetRepository)

    routing {
        createRoute()
        favoriteRoutes(favoriteService)
        poetRoutes(poetService)
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