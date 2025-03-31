package mobin.shabanifar.routes

import io.ktor.server.request.receive
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import mobin.shabanifar.models.respondApi
import mobin.shabanifar.models.user.AuthenticateRequest
import mobin.shabanifar.service.AuthenticateService

fun Route.authenticateRoutes(service: AuthenticateService) {
    route("/api") {
        post("/authenticate") {
            val authenticateRequest = call.receive<AuthenticateRequest>()
            val response = service.authenticateUser(authenticateRequest)
            call.respondApi(response)
        }
    }
}
