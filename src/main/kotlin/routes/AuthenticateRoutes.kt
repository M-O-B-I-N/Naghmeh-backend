package mobin.shabanifar.routes

import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mobin.shabanifar.models.user.AuthenticateRequest
import mobin.shabanifar.service.AuthenticateService

fun Route.authenticateRoutes(service: AuthenticateService) {

    route("/api") {
        post("/authenticate") {
            val authenticateRequest = call.receive<AuthenticateRequest>()
            val response = service.authenticateUser(authenticateRequest)
            call.respond(response.statusCode, response)
        }
    }
}