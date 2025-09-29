package `is`.hi.hbv501g.repx

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository : JpaRepository<User, UUID> {
    /** Checks if email exists. case-insensitive. */
    fun existsByEmailIgnoreCase(email: String): Boolean
}
