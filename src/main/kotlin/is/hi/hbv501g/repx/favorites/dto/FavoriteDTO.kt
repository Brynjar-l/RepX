package `is`.hi.hbv501g.repx.favorites.dto

import java.time.OffsetDateTime
import java.util.*

data class AddFavoriteRequest(
    val userId: UUID,
    val exerciseId: UUID
)

data class FavoriteDTO(
    val userId: UUID,
    val exerciseId: UUID,
    val exerciseName: String,
    val createdAt: OffsetDateTime?
)
