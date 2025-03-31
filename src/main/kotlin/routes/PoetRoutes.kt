package mobin.shabanifar.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import mobin.shabanifar.models.ApiResponse
import mobin.shabanifar.models.respondApi
import mobin.shabanifar.service.PoetService

fun Route.poetRoutes(poetService: PoetService) {
    route("/api/poet") {
        get("/getPoetsByCentury") {
            try {
                // Extract query parameter
                val century = call.request.queryParameters["century"]?.toIntOrNull()

                // Validate query parameter
                if (century == null) {
                    call.respondApi(
                        ApiResponse.Error<Unit>(
                            status = HttpStatusCode.BadRequest,
                            message = "Missing or invalid query parameter: 'century' must be a valid integer"
                        )
                    )
                    return@get
                }

                // Fetch poets for the specified century
                val poetsByCentury = poetService.getPoetsByCentury(century)

                // Respond with the result
                call.respondApi(poetsByCentury)
            } catch (e: Exception) {
                call.respondApi(
                    ApiResponse.Error<Unit>(
                        status = HttpStatusCode.InternalServerError,
                        message = "An error occurred: ${e.message}"
                    )
                )
            }
        }

        get("/getTop8FamousPoets") {
            try {
                val getTop8FamousPoets = poetService.getTop8FamousPoets()
                call.respondApi(getTop8FamousPoets)
            } catch (e: Exception) {
                call.respondApi(
                    ApiResponse.Error<Unit>(
                        status = HttpStatusCode.InternalServerError,
                        message = "An error occurred: ${e.message}"
                    )
                )
            }
        }

        get("/getWorksOfPoet") {
            try {
                // Extract query parameter
                val poetName = call.request.queryParameters["poetName"]

                // Validate query parameter
                if (poetName == null) {
                    call.respondApi(
                        ApiResponse.Error<Unit>(
                            status = HttpStatusCode.BadRequest,
                            message = "Missing or invalid query parameter: 'poetName' must be a valid string"
                        )
                    )
                    return@get
                }

                val getWorksOfPoet = poetService.getWorksOfPoet(poetName)

                // Respond with the result
                call.respondApi(getWorksOfPoet)
            } catch (e: Exception) {
                call.respondApi(
                    ApiResponse.Error<Unit>(
                        status = HttpStatusCode.InternalServerError,
                        message = "An error occurred: ${e.message}"
                    )
                )
            }
        }

        // Get all images for a specific poet
        get("/{poetId}/images") {
            val poetId = call.parameters["poetId"]?.toIntOrNull()
            if (poetId == null) {
                call.respondApi(
                    ApiResponse.Error<Unit>(
                        status = HttpStatusCode.BadRequest,
                        message = "Invalid poet ID"
                    )
                )
                return@get
            }

            val images = poetService.getPoetImages(poetId)

            call.respondApi(images)
        }

        // Get a specific poet with its images
        get("/{poetId}/with-images") {
            val poetId = call.parameters["poetId"]?.toIntOrNull()
            if (poetId == null) {
                call.respondApi(
                    ApiResponse.Error<Unit>(
                        status = HttpStatusCode.BadRequest,
                        message = "Invalid poet ID"
                    )
                )
                return@get
            }

            val poetWithImages = poetService.getPoetWithImages(poetId)

            call.respondApi(poetWithImages)
        }
    }
}
