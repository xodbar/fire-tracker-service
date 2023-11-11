package app.core.user

import java.io.Serializable

data class User(
    val id: Long,
    val username: String,
    val password: String,
    val isActive: Boolean
) : Serializable
