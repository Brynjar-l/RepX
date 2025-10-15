package `is`.hi.hbv501g.repx.workouts.dto

import java.time.Instant
import java.util.*

data class CreateSetRequest(
    val setIndex: Int,
    val reps: Int? = null,
    val weightKg: Double? = null,
    val rir: Int? = null,
    val durationSec: Int? = null,
    val notes: String? = null
)

data class CreateWorkoutExerciseRequest(
    val exerciseId: UUID,
    val orderIndex: Int = 0,
    val sets: List<CreateSetRequest> = emptyList()
)

data class CreateWorkoutRequest(
    val userId: UUID,
    val title: String,
    val startTime: Instant,
    val endTime: Instant? = null,
    val notes: String? = null,
    val exercises: List<CreateWorkoutExerciseRequest> = emptyList()
)

data class SetDTO(
    val id: UUID,
    val setIndex: Int,
    val reps: Int?,
    val weightKg: Double?,
    val rir: Int?,
    val durationSec: Int?,
    val notes: String?
)

data class WorkoutExerciseDTO(
    val id: UUID,
    val exerciseId: UUID,
    val exerciseName: String,
    val orderIndex: Int,
    val sets: List<SetDTO>
)

data class WorkoutDTO(
    val id: UUID,
    val userId: UUID,
    val title: String,
    val startTime: Instant,
    val endTime: Instant?,
    val notes: String?,
    val exercises: List<WorkoutExerciseDTO>
)
