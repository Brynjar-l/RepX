package `is`.hi.hbv501g.repx.users.repo

import `is`.hi.hbv501g.repx.users.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository : JpaRepository<User, UUID> {
    fun existsByEmail(email: String): Boolean
}
