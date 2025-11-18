package `is`.hi.hbv501g.repx.workouts.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.Instant
import java.time.LocalDate
import java.util.*

data class CreateSetRequest(
    val setIndex: Int,
    val reps: Int? = null,
    val weightKg: Double? = null,
    val rir: Int? = null,
    val durationSec: Int? = null,
    val distanceM: Double? = null,
    val type: String? = null,
    val notes: String? = null
)

data class CreateWorkoutExerciseRequest(
    val exerciseId: UUID,
    val orderIndex: Int? = null,
    val sets: List<CreateSetRequest> = emptyList()
)

data class CreateWorkoutRequest(
    val userId: UUID,

    @field:NotBlank
    @field:Size(min = 2, max = 100)
    val title: String,

    val startTime: Instant? = null,
    val endTime: Instant? = null,
    val notes: String? = null,
    val exercises: List<CreateWorkoutExerciseRequest> = emptyList()
)

data class CreateFromTemplateRequest(
    val userId: UUID,
    @field:NotBlank
    @field:Size(min = 2, max = 100)
    val title: String,
    val startTime: Instant? = null,
    val endTime: Instant? = null,
    val notes: String? = null,
    val exercises: List<CreateWorkoutExerciseRequest> = emptyList()
)

data class UpdateWorkoutRequest(
    val title: String? = null,
    val startTime: Instant? = null,
    val endTime: Instant? = null,
    val notes: String? = null
)

data class SetDTO(
    val id: UUID,
    val setIndex: Int,
    val reps: Int?,
    val weightKg: Double?,
    val rir: Int?,
    val durationSec: Int?,
    val distanceM: Double?,
    val type: String?,
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

data class CopyWorkoutRequest(
    val newStartTime: Instant? = null,
    val titleOverride: String? = null
)

data class ExerciseOrderUpdate(
    val workoutExerciseId: UUID,
    val orderIndex: Int
)

data class ReorderExercisesRequest(
    val items: List<ExerciseOrderUpdate>
)

data class PersonalRecordDTO(
    val exerciseId: UUID,
    val exerciseName: String,
    val heaviestWeightKg: Double?,
    val bestReps: Int?,
    val bestVolumeKg: Double?
)

data class WeeklyVolumeDTO(
    val weekStart: LocalDate,
    val totalSets: Int,
    val totalReps: Int,
    val totalVolumeKg: Double
)
