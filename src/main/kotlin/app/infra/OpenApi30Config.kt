package app.infra

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.info.License
import io.swagger.v3.oas.annotations.security.SecurityScheme
import org.springframework.context.annotation.Configuration

@Configuration
@OpenAPIDefinition(
    info = Info(
        title = "fire-tracker-service",
        version = "v1.0 BETA",
        contact = Contact(
            name = "IITU", email = "29316@edu.iitu.kz", url = "https://iitu.kz"
        ),
        license = License(
            name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0"
        ),
        termsOfService = "/health",
        description = "Simple API for tracking fire in real-time data"
    )
)
@SecurityScheme(
    name = "Bearer Authentication",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer"
)
class OpenApi30Config
