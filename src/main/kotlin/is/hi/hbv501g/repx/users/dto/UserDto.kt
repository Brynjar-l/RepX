package `is`.hi.hbv501g.repx.users.dto

import java.time.Instant
import java.util.UUID

data class UserDTO(
    val id: UUID,
    val email: String,
    val displayName: String,
    val createdAt: Instant
)
