package `is`.hi.hbv501g.repx.users.dto

import java.time.OffsetDateTime
import java.util.*

data class UserDTO(
    val id: UUID,
    val email: String,
    val displayName: String,
    val createdAt: OffsetDateTime
)
