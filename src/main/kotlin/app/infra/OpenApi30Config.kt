package app.infra

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.info.License
import io.swagger.v3.oas.annotations.servers.Server
import org.springframework.context.annotation.Configuration

@Configuration
@OpenAPIDefinition(
    info = Info(
        title = "fire-tracker-service",
        version = "\${api.version}",
        contact = Contact(
            name = "IITU", email = "29316@edu.iitu.kz", url = "https://iitu.kz"
        ),
        license = License(
            name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0"
        ),
        termsOfService = "Default",
        description = "Simple API for tracking fire in real-time data"
    ),
    servers = [Server(
        url = "\${api.server.url}",
        description = "Production"
    )]
)
class OpenApi30Config
