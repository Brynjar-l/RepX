package `is`.hi.hbv501g.repx.workouts.service

import `is`.hi.hbv501g.repx.exercises.domain.Exercise
import `is`.hi.hbv501g.repx.exercises.repo.ExerciseRepository
import `is`.hi.hbv501g.repx.users.domain.User
import `is`.hi.hbv501g.repx.users.repo.UserRepository
import `is`.hi.hbv501g.repx.workouts.domain.LiftSet
import `is`.hi.hbv501g.repx.workouts.domain.Workout
import `is`.hi.hbv501g.repx.workouts.domain.WorkoutExercise
import `is`.hi.hbv501g.repx.workouts.dto.*
import `is`.hi.hbv501g.repx.workouts.repo.WorkoutRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.*

@Service
class WorkoutService(
    private val workoutRepo: WorkoutRepository,
    private val userRepo: UserRepository,
    private val exerciseRepo: ExerciseRepository
) {

    @Transactional
    fun createWorkout(req: CreateWorkoutRequest): WorkoutDTO {
        val user = findUser(req.userId)
        val workout = Workout(
            user = user,
            title = req.title.trim(),
            startTime = req.startTime ?: Instant.now(),
            endTime = req.endTime,
            notes = req.notes
        )

        buildExercisesAndSets(workout, req.exercises)

        val saved = workoutRepo.save(workout)
        return saved.toDTO()
    }

    @Transactional
    fun createFromTemplate(req: CreateFromTemplateRequest): WorkoutDTO {
        val createReq = CreateWorkoutRequest(
            userId = req.userId,
            title = req.title,
            startTime = req.startTime,
            endTime = req.endTime,
            notes = req.notes,
            exercises = req.exercises
        )
        return createWorkout(createReq)
    }

    @Transactional(readOnly = true)
    fun getWorkout(id: UUID): WorkoutDTO? =
        workoutRepo.findById(id).orElse(null)?.toDTO()

    @Transactional
    fun updateWorkout(id: UUID, req: UpdateWorkoutRequest): WorkoutDTO {
        val workout = workoutRepo.findById(id)
            .orElseThrow { IllegalArgumentException("workout_not_found") }

        req.title?.let { workout.title = it.trim() }
        req.startTime?.let { workout.startTime = it }
        req.endTime?.let { workout.endTime = it }
        if (req.notes != null) workout.notes = req.notes

        return workout.toDTO()
    }

    @Transactional
    fun deleteWorkout(id: UUID): Boolean {
        return if (workoutRepo.existsById(id)) {
            workoutRepo.deleteById(id)
            true
        } else false
    }


    @Transactional(readOnly = true)
    fun history(userId: UUID, pageable: Pageable): Page<WorkoutDTO> =
        workoutRepo.findByUserIdOrderByStartTimeDesc(userId, pageable)
            .map { it.toDTO() }

    @Transactional(readOnly = true)
    fun exerciseHistory(exerciseId: UUID, userId: UUID?, pageable: Pageable): Page<WorkoutDTO> =
        workoutRepo.findHistoryForExercise(exerciseId, userId, pageable)
            .map { it.toDTO() }


    @Transactional
    fun copyWorkout(originalId: UUID, req: CopyWorkoutRequest): WorkoutDTO {
        val original = workoutRepo.findById(originalId)
            .orElseThrow { IllegalArgumentException("workout_not_found") }

        val copy = Workout(
            user = original.user,
            title = req.titleOverride?.trim().takeUnless { it.isNullOrEmpty() } ?: original.title,
            startTime = req.newStartTime ?: Instant.now(),
            endTime = null,
            notes = original.notes
        )

        original.exercises
            .sortedBy { it.orderIndex }
            .forEach { oe ->
                val we = WorkoutExercise(
                    workout = copy,
                    exercise = oe.exercise,
                    orderIndex = oe.orderIndex
                )

                oe.sets.sortedBy { it.setIndex }.forEach { os ->
                    we.sets.add(
                        LiftSet(
                            workoutExercise = we,
                            setIndex = os.setIndex,
                            reps = os.reps,
                            weightKg = os.weightKg,
                            rir = os.rir,
                            durationSec = os.durationSec,
                            distanceM = os.distanceM,
                            type = os.type,
                            notes = os.notes
                        )
                    )
                }

                copy.exercises.add(we)
            }

        return workoutRepo.save(copy).toDTO()
    }

    @Transactional
    fun reorderExercises(workoutId: UUID, req: ReorderExercisesRequest) {
        val workout = workoutRepo.findById(workoutId)
            .orElseThrow { IllegalArgumentException("workout_not_found") }

        val byId = workout.exercises.associateBy { it.id!! }.toMutableMap()

        req.items.forEach { upd ->
            val we = byId[upd.workoutExerciseId]
                ?: throw IllegalArgumentException("exercise_not_in_workout")
            we.orderIndex = upd.orderIndex
        }
    }


    @Transactional(readOnly = true)
    fun personalRecords(userId: UUID): List<PersonalRecordDTO> {
        val rows = workoutRepo.allSetsForUser(userId)

        data class Agg(
            var name: String,
            var maxW: Double? = null,
            var maxReps: Int? = null,
            var maxVol: Double? = null
        )

        val map = mutableMapOf<UUID, Agg>()

        for (row in rows) {
            val exId = row[0] as UUID
            val exName = row[1] as String
            val w = row[2] as Double?
            val r = row[3] as Int?

            val vol = if (w != null && r != null) w * r else null

            val agg = map.getOrPut(exId) { Agg(exName) }

            if (w != null && (agg.maxW == null || w > agg.maxW!!)) agg.maxW = w
            if (r != null && (agg.maxReps == null || r > agg.maxReps!!)) agg.maxReps = r
            if (vol != null && (agg.maxVol == null || vol > agg.maxVol!!)) agg.maxVol = vol
        }

        return map.map { (id, a) ->
            PersonalRecordDTO(
                exerciseId = id,
                exerciseName = a.name,
                heaviestWeightKg = a.maxW,
                bestReps = a.maxReps,
                bestVolumeKg = a.maxVol
            )
        }.sortedBy { it.exerciseName.lowercase() }
    }

    @Transactional(readOnly = true)
    fun weeklyVolume(userId: UUID, from: LocalDate, to: LocalDate): List<WeeklyVolumeDTO> {
        val startInstant = from.atStartOfDay(ZoneOffset.UTC).toInstant()
        val endInstant = to.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant()

        val workouts = workoutRepo.findByUserIdAndStartTimeBetween(userId, startInstant, endInstant)

        data class Agg(var sets: Int = 0, var reps: Int = 0, var vol: Double = 0.0)

        val map = mutableMapOf<LocalDate, Agg>()

        workouts.forEach { w ->
            val date = w.startTime.atZone(ZoneOffset.UTC).toLocalDate()
            val weekStart = date.minusDays(((date.dayOfWeek.value + 6) % 7).toLong()) // Monday

            val agg = map.getOrPut(weekStart) { Agg() }

            w.exercises.forEach { we ->
                we.sets.forEach { s ->
                    agg.sets++
                    s.reps?.let { r ->
                        agg.reps += r
                        s.weightKg?.let { wKg ->
                            agg.vol += r * wKg
                        }
                    }
                }
            }
        }

        return map.entries.sortedBy { it.key }.map { (weekStart, a) ->
            WeeklyVolumeDTO(
                weekStart = weekStart,
                totalSets = a.sets,
                totalReps = a.reps,
                totalVolumeKg = a.vol
            )
        }
    }


    private fun findUser(id: UUID): User =
        userRepo.findById(id).orElseThrow { IllegalArgumentException("user_not_found") }

    private fun findExercise(id: UUID): Exercise =
        exerciseRepo.findById(id).orElseThrow { IllegalArgumentException("exercise_not_found") }

    private fun buildExercisesAndSets(
        workout: Workout,
        reqs: List<CreateWorkoutExerciseRequest>
    ) {
        workout.exercises.clear()

        reqs.forEachIndexed { idx, exReq ->
            val exercise = findExercise(exReq.exerciseId)
            val orderIdx = exReq.orderIndex ?: idx

            val we = WorkoutExercise(
                workout = workout,
                exercise = exercise,
                orderIndex = orderIdx
            )

            exReq.sets.forEach { sReq ->
                val set = LiftSet(
                    workoutExercise = we,
                    setIndex = sReq.setIndex,
                    reps = sReq.reps,
                    weightKg = sReq.weightKg,
                    rir = sReq.rir,
                    durationSec = sReq.durationSec,
                    distanceM = sReq.distanceM,
                    type = sReq.type,
                    notes = sReq.notes
                )
                we.sets.add(set)
            }

            workout.exercises.add(we)
        }
    }
}


private fun Workout.toDTO(): WorkoutDTO =
    WorkoutDTO(
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

private fun WorkoutExercise.toDTO(): WorkoutExerciseDTO =
    WorkoutExerciseDTO(
        id = this.id!!,
        exerciseId = this.exercise.id!!,
        exerciseName = this.exercise.name,
        orderIndex = this.orderIndex,
        sets = this.sets
            .sortedBy { it.setIndex }
            .map { it.toDTO() }
    )

private fun LiftSet.toDTO(): SetDTO =
    SetDTO(
        id = this.id!!,
        setIndex = this.setIndex,
        reps = this.reps,
        weightKg = this.weightKg,
        rir = this.rir,
        durationSec = this.durationSec,
        distanceM = this.distanceM,
        type = this.type,
        notes = this.notes
    )
