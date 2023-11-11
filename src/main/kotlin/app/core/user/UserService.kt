package app.core.user

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepo: UserRepo,
    private val passwordEncoder: PasswordEncoder
) {

    fun authenticate(username: String, password: String): Boolean {
        val user = userRepo.findFirstByUsername(username) ?: return false
        return passwordEncoder.matches(password, user.password)
    }

    fun getByUsername(username: String) =
        userRepo.findFirstByUsername(username)?.toDto()
}
