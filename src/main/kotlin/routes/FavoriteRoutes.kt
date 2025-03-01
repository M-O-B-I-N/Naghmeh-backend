package mobin.shabanifar.routes

import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mobin.shabanifar.models.BasicApiResponse
import mobin.shabanifar.models.favorite.SaveFavoritePoemRequest
import mobin.shabanifar.service.FavoriteService
import mobin.shabanifar.utils.CustomException
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.favoriteRoutes(favoriteService: FavoriteService) {
    route("/api") {
        post("/saveFavoritePoem") {
            try {
                // Parse the request body
                val request = call.receive<SaveFavoritePoemRequest>()
                val poemId = request.poemId

                if (poemId != null) {
                    transaction {
                        favoriteService.saveFavoritePoem(poemId)
                    }
                    call.respond(HttpStatusCode.OK, BasicApiResponse(true, "Poem saved as favorite"))
                } else {
                    call.respond(HttpStatusCode.BadRequest, "'poemId' parameter cannot be null")
                }
            } catch (e: CustomException) {
                call.respond(e.statusCode, BasicApiResponse(false, e.message ?: "An error occurred"))
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    BasicApiResponse(false, "An error occurred: ${e.message}")
                )
            }
        }
    }
}