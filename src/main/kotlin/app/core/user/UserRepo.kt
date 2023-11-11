package app.core.user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepo : JpaRepository<UserModel, Long> {
    fun findFirstByUsername(username: String): UserModel?
}
