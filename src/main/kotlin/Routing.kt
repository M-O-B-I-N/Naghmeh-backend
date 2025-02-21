package mobin.shabanifar

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mobin.shabanifar.models.*

fun Application.configureRouting() {
    routing {
        createRoute()
    }
}

fun Route.createRoute() {
    route("/api") {

        get("/getPoetsByCentury") {
            try {
                // Extract query parameter
                val century = call.request.queryParameters["century"]?.toIntOrNull()

                // Validate query parameter
                if (century == null) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        "Missing or invalid query parameter: 'century' must be a valid integer"
                    )
                    return@get
                }

                // Fetch poets for the specified century
                val poetsByCentury = getPoetsByCentury(century)

                // Respond with the result
                call.respond(HttpStatusCode.OK, poetsByCentury)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "An error occurred: ${e.message}")
            }
        }

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

        get("/getWorksOfPoet") {
            try {
                // Extract query parameter
                val poetName = call.request.queryParameters["poetName"]

                // Validate query parameter
                if (poetName == null) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        "Missing or invalid query parameter: 'poetName' must be a valid string"
                    )
                    return@get
                }

                val getWorksOfPoet = getWorksOfPoet(poetName)

                // Respond with the result
                call.respond(HttpStatusCode.OK, getWorksOfPoet)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "An error occurred: ${e.message}")
            }
        }

        get("/getTop8FamousPoets") {
            try {
                val getTop8FamousPoets = getTop8FamousPoets()

                // Respond with the result
                call.respond(HttpStatusCode.OK, getTop8FamousPoets)
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

    }
}
