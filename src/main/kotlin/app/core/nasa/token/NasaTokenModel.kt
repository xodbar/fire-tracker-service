package app.core.nasa.token

import jakarta.persistence.*

@Entity
@Table(name = "nasa_refresh_tokens")
class NasaTokenModel(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(name = "token", columnDefinition = "text", unique = true, nullable = false)
    val token: String,

    @Column(name = "active", nullable = false)
    val active: Boolean
)
