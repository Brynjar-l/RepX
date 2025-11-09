package `is`.hi.hbv501g.repx.exercises.dto

import java.util.UUID

data class ExerciseDTO(
    val id: UUID,
    val name: String,
    val primaryMuscle: String?,
    val equipment: String?,
    val difficulty: String?,
    val isPublic: Boolean
)
