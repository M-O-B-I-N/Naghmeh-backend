package mobin.shabanifar.models

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond

sealed class ApiResponse<T>(
    val data: T? = null,
    val message: String? = null,
    val status: HttpStatusCode
) {
    class Success<T>(
        data: T? = null,
        status: HttpStatusCode = HttpStatusCode.OK
    ) : ApiResponse<T>(data, null, status)

    class Error<T>(
        message: String,
        status: HttpStatusCode = HttpStatusCode.BadRequest,
        data: T? = null
    ) : ApiResponse<T>(data, message, status)
}

suspend inline fun <reified T> ApplicationCall.respondApi(response: ApiResponse<T>) {
    val responseBody = mapOf("data" to response.data, "message" to response.message).filterValues { it != null }
    respond(response.status, responseBody)
}
