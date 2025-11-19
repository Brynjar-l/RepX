package `is`.hi.hbv501g.repx.workouts.web

import `is`.hi.hbv501g.repx.workouts.dto.*
import `is`.hi.hbv501g.repx.workouts.service.WorkoutService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.util.*

@RestController
@RequestMapping("/workouts")
class WorkoutController(
    private val service: WorkoutService
) {
    @PostMapping
    fun create(
        @Valid @RequestBody req: CreateWorkoutRequest
    ): ResponseEntity<WorkoutDTO> =
        ResponseEntity.ok(service.createWorkout(req))

    @GetMapping
    fun list(pageable: Pageable): Page<WorkoutDTO> =
        service.listWorkouts(pageable) 

    @PostMapping("/from-template")
    fun createFromTemplate(
        @Valid @RequestBody req: CreateFromTemplateRequest
    ): ResponseEntity<WorkoutDTO> =
        ResponseEntity.ok(service.createFromTemplate(req))

    @GetMapping("/{id}")
    fun get(@PathVariable id: UUID): ResponseEntity<WorkoutDTO> =
        service.getWorkout(id)?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: UUID,
        @RequestBody req: UpdateWorkoutRequest
    ): ResponseEntity<WorkoutDTO> =
        ResponseEntity.ok(service.updateWorkout(id, req))

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): ResponseEntity<Void> =
        if (service.deleteWorkout(id)) ResponseEntity.noContent().build()
        else ResponseEntity.notFound().build()

    @GetMapping("/users/{userId}/history")
    fun history(
        @PathVariable userId: UUID,
        pageable: Pageable
    ): Page<WorkoutDTO> =
        service.history(userId, pageable)

    @GetMapping("/exercises/{exerciseId}/history")
    fun exerciseHistory(
        @PathVariable exerciseId: UUID,
        @RequestParam(required = false) userId: UUID?,
        pageable: Pageable
    ): Page<WorkoutDTO> =
        service.exerciseHistory(exerciseId, userId, pageable)

    @PostMapping("/{workoutId}/copy")
    fun copy(
        @PathVariable workoutId: UUID,
        @RequestBody req: CopyWorkoutRequest
    ): ResponseEntity<WorkoutDTO> =
        ResponseEntity.ok(service.copyWorkout(workoutId, req))

    @PatchMapping("/{workoutId}/exercises/reorder")
    fun reorderExercises(
        @PathVariable workoutId: UUID,
        @RequestBody req: ReorderExercisesRequest
    ): ResponseEntity<Void> {
        service.reorderExercises(workoutId, req)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/users/{userId}/prs")
    fun personalRecords(
        @PathVariable userId: UUID
    ): List<PersonalRecordDTO> =
        service.personalRecords(userId)

    @GetMapping("/users/{userId}/weekly-volume")
    fun weeklyVolume(
        @PathVariable userId: UUID,
        @RequestParam
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        from: LocalDate,
        @RequestParam
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        to: LocalDate
    ): List<WeeklyVolumeDTO> =
        service.weeklyVolume(userId, from, to)
}
