package `is`.hi.hbv501g.repx

import java.time.Instant
import java.util.UUID

data class CreateUserRequest(
    val email: String,
    val password: String,
    val displayName: String = ""
)

data class UserDTO(
    val id: UUID,
    val email: String,
    val displayName: String,
    val createdAt: Instant
)

fun User.toDTO() = UserDTO(
    id = this.id!!, email = this.email, displayName = this.displayName, createdAt = this.createdAt
)
