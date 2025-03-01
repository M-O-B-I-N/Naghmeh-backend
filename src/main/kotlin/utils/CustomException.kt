package mobin.shabanifar.utils

import io.ktor.http.*

class CustomException(val statusCode: HttpStatusCode, message: String) : Exception(message)