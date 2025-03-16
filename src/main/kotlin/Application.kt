package mobin.shabanifar

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.typesafe.config.ConfigFactory
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.config.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mobin.shabanifar.models.Cat
import mobin.shabanifar.models.favorite.Favorite
import mobin.shabanifar.models.poem.Poem
import mobin.shabanifar.models.poet.Poet
import mobin.shabanifar.models.poet.PoetImage
import mobin.shabanifar.models.user.User
import mobin.shabanifar.models.verse.Verse
import mobin.shabanifar.plugins.configureSerialization
import mobin.shabanifar.repository.*
import mobin.shabanifar.routes.*
import mobin.shabanifar.service.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun main() {
    initDatabase()
    embeddedServer(Netty, port = 2003, host = "localhost", module = Application::module).start(wait = true)
}

fun Application.module() {
    configureSerialization()
    configureStatusPages()
    configureSecurity()
    val favoriteRepository = FavoriteRepository()
    val favoriteService = FavoriteService(favoriteRepository)
    val poetRepository = PoetRepository()
    val poetService = PoetService(poetRepository)
    val verseRepository = VerseRepository()
    val verseService = VerseService(verseRepository)
    val poemRepository = PoemRepository()
    val poemService = PoemService(poemRepository)
    val authenticateRepository = AuthenticateRepository()
    val authenticateService = AuthenticateService(authenticateRepository)

    routing {
        authenticate("jwt") {
            favoriteRoutes(favoriteService)
            poetRoutes(poetService)
            verseRoutes(verseService)
            poemRoutes(poemService)
        }
        authenticateRoutes(authenticateService)
    }
}

fun initDatabase() {
    // Connect to the existing SQLite database file
    Database.connect(
        url = "jdbc:sqlite:src/main/kotlin/data/naghmeh.db",
        driver = "org.sqlite.JDBC"
    )
    transaction {
        SchemaUtils.create(Poet, Poem, Cat, Verse, PoetImage, Favorite, User) // Create the table if it doesn't exist
    }
}


fun Application.configureSecurity() {
    val config = HoconApplicationConfig(ConfigFactory.load())
    val jwtSecret = config.property("ktor.jwt.secret").getString()
    val jwtAudience = config.property("ktor.jwt.audience").getString()
    val jwtIssuer = config.property("ktor.jwt.issuer").getString()
    val jwtRealm = config.property("ktor.jwt.realm").getString()

    install(Authentication) {
        jwt("jwt") {
            realm = jwtRealm
            verifier(
                JWT.require(Algorithm.HMAC256(jwtSecret))
                    .withAudience(jwtAudience)
                    .withIssuer(jwtIssuer)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(jwtAudience)) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
        }
    }
}

fun Application.configureStatusPages() {
    install(StatusPages) {
        // Handle exceptions
        exception<Throwable> { call, cause ->
            call.respond(HttpStatusCode.InternalServerError, "Internal Server Error: ${cause.message}")
        }
    }
}