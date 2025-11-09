package `is`.hi.hbv501g.repx.exercises.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateExerciseRequest(
    @field:NotBlank @field:Size(min = 2, max = 100)
    val name: String,

    @field:Size(max = 100)
    val primaryMuscle: String? = null,

    @field:Size(max = 100)
    val equipment: String? = null,

    @field:Size(max = 50)
    val difficulty: String? = null,

    val isPublic: Boolean = true
)
