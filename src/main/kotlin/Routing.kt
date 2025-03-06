package mobin.shabanifar

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mobin.shabanifar.models.*
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.createRoute() {
    route("/api") {

        get("/getPoemsOfCategory") {
            try {
                val poetName = call.request.queryParameters["poetName"]
                val categoryName = call.request.queryParameters["categoryName"]

                // Validate query parameters
                if (poetName == null || categoryName == null) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        "Missing query parameters: poetName and categoryName are required"
                    )
                    return@get
                }

                // Fetch poems of the specified category for the poet
                val poemsOfCategory = getPoemsOfCategory(poetName, categoryName)

                // Respond with the result
                call.respond(HttpStatusCode.OK, poemsOfCategory)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "An error occurred: ${e.message}")
            }
        }

        get("/getRandomVerse") {
            try {
                // Fetch a random verse
                val randomVerse =  getRandomVerse()

                // Respond with the result
                call.respond(HttpStatusCode.OK, randomVerse)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "An error occurred: ${e.message}")
            }
        }

        get("/advancedVerseSearch") {
            try {
                // Extract query parameters
                val verseText = call.request.queryParameters["verseText"]
                val poetName = call.request.queryParameters["poetName"]
                val categoryName = call.request.queryParameters["categoryName"]
                val excludePoetName = call.request.queryParameters["excludePoetName"]
                val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 1 // Default to page 1
                val pageSize =
                    call.request.queryParameters["pageSize"]?.toIntOrNull() ?: 10 // Default to 10 items per page

                // Validate required query parameter
                if (verseText == null) {
                    call.respond(HttpStatusCode.BadRequest, "Missing required query parameter: verseText")
                    return@get
                }

                // Perform the advanced search
                val result = advancedVerseSearch(verseText, poetName, categoryName, excludePoetName, page, pageSize)

                // Respond with the result
                call.respond(HttpStatusCode.OK, result)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "An error occurred: ${e.message}")
            }
        }

        get("/getVersesOfPoem") {
            try {
                // Extract query parameters
                val poetName = call.request.queryParameters["poetName"]
                val categoryName = call.request.queryParameters["categoryName"]
                val poemTitle = call.request.queryParameters["poemTitle"]

                // Validate query parameters
                if (poetName == null || categoryName == null || poemTitle == null) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        "Missing query parameters: poetName, categoryName, and poemTitle are required"
                    )
                    return@get
                }

                // Fetch the verses of the specified poem
                val verses = getVersesOfPoem(poetName, categoryName, poemTitle)

                // Respond with the verses
                call.respond(HttpStatusCode.OK, verses)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "An error occurred: ${e.message}")
            }
        }

        // Get all images for a specific poet
        get("/poet/{poetId}/images") {
            val poetId = call.parameters["poetId"]?.toIntOrNull()
            if (poetId == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid poet ID")
                return@get
            }

            val images = withContext(Dispatchers.IO) {
                transaction {
                    PoetImage.select { PoetImage.poetId eq poetId }.map {
                        PoetImageResponse(
                            id = it[PoetImage.id],
                            poetId = it[PoetImage.poetId],
                            url = it[PoetImage.url]
                        )
                    }
                }
            }

            call.respond(HttpStatusCode.OK, images)
        }

        // Get a specific poet with its images
        get("/poet/{poetId}/with-images") {
            val poetId = call.parameters["poetId"]?.toIntOrNull()
            if (poetId == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid poet ID")
                return@get
            }

            val poetWithImages = getPoetWithImages(poetId)

            if (poetWithImages == null) {
                call.respond(HttpStatusCode.NotFound, "Poet not found")
            } else {
                call.respond(HttpStatusCode.OK, poetWithImages)
            }
        }

    }
}
