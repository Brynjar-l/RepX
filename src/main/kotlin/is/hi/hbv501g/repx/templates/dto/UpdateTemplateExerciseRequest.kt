package `is`.hi.hbv501g.repx.templates.dto

import java.util.UUID

data class UpdateTemplateExerciseRequest(
    val orderIndex: Int? = null,
    val defaultReps: Int? = null,
    val defaultWeightKg: Double? = null
)

data class ReorderTemplateExercisesRequest(
    val orderedIds: List<UUID>
)
