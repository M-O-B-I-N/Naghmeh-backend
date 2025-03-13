package mobin.shabanifar.models.user

data class AuthenticateRequest(
    val name: String?,
    val email: String,
    val password: String
)
