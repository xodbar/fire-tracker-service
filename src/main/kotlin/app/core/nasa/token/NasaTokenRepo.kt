package app.core.nasa.token

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NasaTokenRepo : JpaRepository<NasaTokenModel, Long> {
    fun findFirstByActiveIsTrueOrderByIdDesc(): NasaTokenModel
}
