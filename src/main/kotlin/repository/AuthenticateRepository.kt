package mobin.shabanifar.repository

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.typesafe.config.ConfigFactory
import io.ktor.http.*
import io.ktor.server.config.*
import mobin.shabanifar.models.ApiResponse
import mobin.shabanifar.models.user.AuthenticateRequest
import mobin.shabanifar.models.user.AuthenticateResponse
import mobin.shabanifar.models.user.User
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.mindrot.jbcrypt.BCrypt

class AuthenticateRepository {
    val config = HoconApplicationConfig(ConfigFactory.load())
    val jwtSecret = config.property("ktor.jwt.secret").getString()
    val jwtAudience = config.property("ktor.jwt.audience").getString()
    val jwtIssuer = config.property("ktor.jwt.issuer").getString()
    val jwtRealm = config.property("ktor.jwt.realm").getString()


    fun authenticateUser(authenticateRequest: AuthenticateRequest): ApiResponse<AuthenticateResponse> {
        val user = transaction {
            User.select { User.email eq authenticateRequest.email }.singleOrNull()
        }

        if (user == null) {
            // Sign up: Create a new user
            val hashedPassword = BCrypt.hashpw(authenticateRequest.password, BCrypt.gensalt())
            transaction {
                User.insert {
                    it[name] = authenticateRequest.name
                    it[email] = authenticateRequest.email
                    it[password] = hashedPassword
                }
            }
            val token = JWT.create()
                .withAudience(jwtAudience)
                .withIssuer(jwtIssuer)
                .withClaim("email", authenticateRequest.email)
                .sign(Algorithm.HMAC256(jwtSecret))
            return ApiResponse.Success(status = HttpStatusCode.Created, data = AuthenticateResponse(token))

        } else {

            // Login: Validate password
            return if (BCrypt.checkpw(authenticateRequest.password, user[User.password])) {
                val token = JWT.create()
                    .withAudience(jwtAudience)
                    .withIssuer(jwtIssuer)
                    .withClaim("email", authenticateRequest.email)
                    .sign(Algorithm.HMAC256(jwtSecret))
                ApiResponse.Success(data = AuthenticateResponse(token))
            } else {
                ApiResponse.Error(status = HttpStatusCode.Unauthorized, message = "Password is incorrect!")
            }
        }
    }

}
