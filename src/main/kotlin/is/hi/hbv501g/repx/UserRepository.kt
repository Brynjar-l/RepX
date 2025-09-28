package is.hi.hbv501g.repx

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserRepository : JpaRepository<User, UUID> {
    fun existsByEmailIgnoreCase(email: String): Boolean
}
