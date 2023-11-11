package app.web.filter

import app.core.exception.InvalidTokenException
import app.core.jwt.JWTAuthService
import app.core.user.UserService
import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class JWTAuthFilter(
    private val jwtAuthService: JWTAuthService,
    private val userService: UserService
) : Filter {

    private val publicRoutes = listOf(
        "/health",
        "/auth/issue-token",
        "/swagger-ui/index.html",
        "/api/docs",
        "/swagger-ui",
        "/v3"
    )

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain) {
        val req = request as HttpServletRequest

        if (publicRoutes.any { req.requestURL.contains(it) }) {
            chain.doFilter(req, response)
            return
        }

        req.getHeader("Authorization")?.let { jwtToken ->
            val rawToken = jwtToken.removePrefix("Bearer ")
            jwtAuthService.getSubject(rawToken)?.let { username ->
                val isActive = userService.getByUsername(username)?.isActive == true
                if (isActive) {
                    chain.doFilter(request, response)
                    return
                }
            }
        }

        throw InvalidTokenException()
    }
}
