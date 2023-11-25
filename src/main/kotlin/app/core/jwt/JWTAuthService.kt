package app.core.jwt

import app.core.utils.getCurrentAlmatyLocalDateTime
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.ZoneOffset
import java.util.*

@Service
class JWTAuthService(
    @Qualifier("jwtSignAlgorithm") private val algorithm: Algorithm,
    @Qualifier("jwtVerifier") private val verifier: JWTVerifier,
    @Value("\${app.auth.expiration-millis}") val expirationMillis: Long,
    @Value("\${app.auth.issuer}") val issuer: String
) {

    fun issueApiToken(username: String): String {
        val currentInstant = getCurrentAlmatyLocalDateTime().toInstant(ZoneOffset.UTC)
        return JWT.create()
            .withIssuer(issuer)
            .withSubject(username)
            .withIssuedAt(currentInstant)
            .withExpiresAt(currentInstant.plusMillis(expirationMillis))
            .withJWTId(UUID.randomUUID().toString())
            .withNotBefore(currentInstant.plusMillis(1000))
            .sign(algorithm)
    }

    fun getSubject(token: String): String? {
        return try {
            val decoded = verifier.verify(token)
            decoded.subject
        } catch (ex: Exception) {
            ex.printStackTrace()
            return null
        }
    }
}
