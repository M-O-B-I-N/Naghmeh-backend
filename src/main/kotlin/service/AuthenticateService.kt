package mobin.shabanifar.service

import mobin.shabanifar.models.ApiResponse
import mobin.shabanifar.models.user.AuthenticateRequest
import mobin.shabanifar.models.user.AuthenticateResponse
import mobin.shabanifar.repository.AuthenticateRepository

class AuthenticateService(private val repository: AuthenticateRepository) {

    fun authenticateUser(authenticateRequest: AuthenticateRequest): ApiResponse<AuthenticateResponse> {
        return repository.authenticateUser(authenticateRequest)
    }
}
