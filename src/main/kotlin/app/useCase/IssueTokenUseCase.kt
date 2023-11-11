package app.useCase

import app.core.jwt.JWTAuthService
import app.core.user.UserService
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.io.Serializable

@Component
class IssueTokenUseCase(
    private val userService: UserService,
    private val JWTAuthService: JWTAuthService
) {

    @Transactional
    fun handle(input: IssueTokenInput): IssueTokenOutput {
        return userService.authenticate(input.username, input.password).takeIf { it }?.let {
            return IssueTokenOutput(
                type = "bearer",
                token = JWTAuthService.issueApiToken(input.username)
            )
        } ?: IssueTokenOutput(
            type = "invalid credentials",
            token = null
        )
    }
}

data class IssueTokenInput(
    val username: String,
    val password: String
) : Serializable

data class IssueTokenOutput(
    val type: String,
    val token: String?
) : Serializable
