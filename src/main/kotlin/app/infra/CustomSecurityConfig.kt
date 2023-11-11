package app.infra

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class CustomSecurityConfig(
    @Value("\${app.auth.secret}") val jwtSecret: String,
) {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun jwtSignAlgorithm(): Algorithm {
        return Algorithm.HMAC256(jwtSecret)
    }

    @Bean
    fun jwtVerifier(): JWTVerifier {
        return JWT.require(jwtSignAlgorithm())
            .withIssuer("IITU")
            .build()
    }
}
