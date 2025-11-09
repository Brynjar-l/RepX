package `is`.hi.hbv501g.repx.templates.dto

import java.util.UUID

data class CreateTemplateExerciseRequest(
    val exerciseId: UUID,
    val orderIndex: Int = 0,
    val defaultReps: Int? = null,
    val defaultWeightKg: Double? = null
)

data class CreateTemplateRequest(
    val userId: UUID,
    val name: String,
    val notes: String? = null,
    val exercises: List<CreateTemplateExerciseRequest> = emptyList()
)

data class UpdateTemplateRequest(
    val name: String? = null,
    val notes: String? = null
)

data class TemplateExerciseDTO(
    val id: UUID,
    val exerciseId: UUID,
    val exerciseName: String,
    val orderIndex: Int,
    val defaultReps: Int?,
    val defaultWeightKg: Double?
)

data class TemplateDTO(
    val id: UUID,
    val userId: UUID,
    val name: String,
    val notes: String?,
    val exercises: List<TemplateExerciseDTO>
)
