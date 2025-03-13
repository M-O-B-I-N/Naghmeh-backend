package mobin.shabanifar.models

import com.google.gson.annotations.SerializedName
import io.ktor.http.*

data class ApiResponse<T>(
    @SerializedName("message") val message: String? = null,
    @SerializedName("data") val data: T? = null,
    @SerializedName("statusCode") val statusCode: HttpStatusCode,
)
