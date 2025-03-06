package mobin.shabanifar.routes

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mobin.shabanifar.service.PoetService

fun Route.poetRoutes(poetService: PoetService) {
    route("/api/poet") {

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
                val poetsByCentury = poetService.getPoetsByCentury(century)

                // Respond with the result
                call.respond(HttpStatusCode.OK, poetsByCentury)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "An error occurred: ${e.message}")
            }
        }

        get("/getTop8FamousPoets") {
            try {
                val getTop8FamousPoets = poetService.getTop8FamousPoets()

                // Respond with the result
                call.respond(HttpStatusCode.OK, getTop8FamousPoets)
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

                val getWorksOfPoet = poetService.getWorksOfPoet(poetName)

                // Respond with the result
                call.respond(HttpStatusCode.OK, getWorksOfPoet)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "An error occurred: ${e.message}")
            }
        }
    }
}