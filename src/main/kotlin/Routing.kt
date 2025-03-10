package mobin.shabanifar

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mobin.shabanifar.models.getPoemsOfCategory

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

    }
}
