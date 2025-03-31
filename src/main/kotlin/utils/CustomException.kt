package mobin.shabanifar.utils

import io.ktor.http.HttpStatusCode

class CustomException(val statusCode: HttpStatusCode, message: String) : Exception(message)
