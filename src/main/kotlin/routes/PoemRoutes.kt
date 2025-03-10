package mobin.shabanifar.routes

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mobin.shabanifar.service.PoemService

fun Route.poemRoutes(service: PoemService) {
    route("/api/poem") {
        get("/getPoemsOfCategory") {
            try {
                val poetName = call.request.queryParameters["poetName"]
                val categoryName = call.request.queryParameters["categoryName"]
                val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 1 // Default to page 1
                val pageSize =
                    call.request.queryParameters["pageSize"]?.toIntOrNull() ?: 10 // Default to 10 items per page

                // Validate query parameters
                if (poetName == null || categoryName == null) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        "Missing query parameters: poetName and categoryName are required"
                    )
                    return@get
                }

                // Fetch poems of the specified category for the poet
                val poemsOfCategory = service.getPoemsOfCategory(
                    poetName =poetName,
                    categoryName = categoryName,
                    page = page,
                    pageSize = pageSize
                )

                // Respond with the result
                call.respond(HttpStatusCode.OK, poemsOfCategory)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "An error occurred: ${e.message}")
            }
        }

    }
}