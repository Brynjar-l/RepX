package `is`.hi.hbv501g.repx.workouts.service

import `is`.hi.hbv501g.repx.exercises.repo.ExerciseRepository
import `is`.hi.hbv501g.repx.users.repo.UserRepository
import `is`.hi.hbv501g.repx.workouts.domain.LiftSet
import `is`.hi.hbv501g.repx.workouts.domain.Workout
import `is`.hi.hbv501g.repx.workouts.domain.WorkoutExercise
import `is`.hi.hbv501g.repx.workouts.dto.CreateWorkoutRequest
import `is`.hi.hbv501g.repx.workouts.dto.SetDTO
import `is`.hi.hbv501g.repx.workouts.dto.WorkoutDTO
import `is`.hi.hbv501g.repx.workouts.dto.WorkoutExerciseDTO
import `is`.hi.hbv501g.repx.workouts.repo.WorkoutRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

@Service
class WorkoutService(
    private val workoutRepo: WorkoutRepository,
    private val userRepo: UserRepository,
    private val exerciseRepo: ExerciseRepository
) {
    @Transactional
    fun create(req: CreateWorkoutRequest): WorkoutDTO {
        val user = userRepo.findById(req.userId).orElseThrow { IllegalArgumentException("user_not_found") }

        val workout = Workout(
            user = user,
            title = req.title,
            startTime = req.startTime,
            endTime = req.endTime,
            notes = req.notes
        )

        req.exercises.forEach { exReq ->
            val exercise = exerciseRepo.findById(exReq.exerciseId)
                .orElseThrow { IllegalArgumentException("exercise_not_found") }

            val wex = WorkoutExercise(
                workout = workout,
                exercise = exercise,
                orderIndex = exReq.orderIndex
            )

            exReq.sets.forEach { sReq ->
                val weightBd: BigDecimal? = sReq.weightKg
                    ?.let { BigDecimal.valueOf(it).setScale(2, RoundingMode.HALF_UP) }

                val set = LiftSet(
                    workoutExercise = wex,
                    setIndex = sReq.setIndex,
                    reps = sReq.reps,
                    weightKg = weightBd,
                    rir = sReq.rir,
                    durationSec = sReq.durationSec,
                    notes = sReq.notes
                )
                wex.sets.add(set)
            }

            workout.exercises.add(wex)
        }

        return workoutRepo.save(workout).toDTO()
    }

    @Transactional(readOnly = true)
    fun get(id: UUID): WorkoutDTO? =
        workoutRepo.findWithGraphById(id).orElse(null)?.toDTO()

    @Transactional(readOnly = true)
    fun list(pageable: Pageable): Page<WorkoutDTO> =
        workoutRepo.findAll(pageable).map { it.toDTO() }

    @Transactional(readOnly = true)
    fun listByUser(userId: UUID, pageable: Pageable): Page<WorkoutDTO> =
        workoutRepo.findByUser_Id(userId, pageable).map { it.toDTO() }

    @Transactional
    fun delete(id: UUID): Boolean =
        if (workoutRepo.existsById(id)) { workoutRepo.deleteById(id); true } else false
}

private fun `is`.hi.hbv501g.repx.workouts.domain.Workout.toDTO() = WorkoutDTO(
    id = this.id!!,
    userId = this.user.id!!,
    title = this.title,
    startTime = this.startTime,
    endTime = this.endTime,
    notes = this.notes,
    exercises = this.exercises
        .sortedBy { it.orderIndex }
        .map { it.toDTO() }
)

private fun `is`.hi.hbv501g.repx.workouts.domain.WorkoutExercise.toDTO() = WorkoutExerciseDTO(
    id = this.id!!,
    exerciseId = this.exercise.id!!,
    exerciseName = this.exercise.name,
    orderIndex = this.orderIndex,
    sets = this.sets
        .sortedBy { it.setIndex }
        .map { it.toDTO() }
)

private fun `is`.hi.hbv501g.repx.workouts.domain.LiftSet.toDTO() = SetDTO(
    id = this.id!!,
    setIndex = this.setIndex,
    reps = this.reps,
    weightKg = this.weightKg?.toDouble(),
    rir = this.rir,
    durationSec = this.durationSec,
    notes = this.notes
)
