package app.core.externalServiceRequest

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ExternalServiceRequestRepo : JpaRepository<ExternalServiceRequestModel, Long>
