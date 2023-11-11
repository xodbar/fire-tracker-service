package app.core.user

import jakarta.persistence.*

@Entity
@Table(name = "users")
class UserModel(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(name = "username", unique = true, nullable = false)
    val username: String,

    @Column(name = "password", nullable = false)
    val password: String,

    @Column(name = "is_active", nullable = false)
    val isActive: Boolean
) {
    fun toDto() = User(
        id = id,
        username = username,
        password = password,
        isActive = isActive
    )
}
