package `is`.hi.hbv501g.repx

import java.time.Instant
import java.util.*


/** Holds all information that's needed to create a new User. */
data class CreateUserRequest(
    val email: String,
    val password: String,
    val displayName: String = ""
)
/** A user's public information */
data class UserDTO(
    val id: UUID,
    val email: String,
    val displayName: String,
    val createdAt: Instant
)

/** Maps entity to simple DTO. */
fun User.toDTO() = UserDTO(
    id = this.id!!, email = this.email, displayName = this.displayName, createdAt = this.createdAt
)
