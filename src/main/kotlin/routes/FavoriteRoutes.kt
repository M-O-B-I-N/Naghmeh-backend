package mobin.shabanifar.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import mobin.shabanifar.models.ApiResponse
import mobin.shabanifar.models.favorite.SaveFavoritePoemRequest
import mobin.shabanifar.models.respondApi
import mobin.shabanifar.service.FavoriteService
import mobin.shabanifar.utils.CustomException
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.favoriteRoutes(favoriteService: FavoriteService) {
    route("/api/favorite") {
        post("/saveFavoritePoem") {
            try {
                // Parse the request body
                val request = call.receive<SaveFavoritePoemRequest>()
                val poemId = request.poemId

                if (poemId != null) {
                    transaction {
                        favoriteService.saveFavoritePoem(poemId)
                    }
                    call.respondApi(ApiResponse.Success<Unit>(status = HttpStatusCode.OK))
                } else {
                    call.respondApi(
                        ApiResponse.Error<Unit>(
                            status = HttpStatusCode.BadRequest,
                            message = "'poemId' parameter cannot be null"
                        )
                    )
                }
            } catch (e: CustomException) {
                call.respondApi(
                    ApiResponse.Error<Unit>(
                        status = e.statusCode,
                        message = e.message ?: "An error occurred"
                    )
                )
            } catch (e: Exception) {
                call.respondApi(
                    ApiResponse.Error<Unit>(
                        status = HttpStatusCode.InternalServerError,
                        message = "An error occurred: ${e.message}"
                    )
                )
            }
        }
    }
}
