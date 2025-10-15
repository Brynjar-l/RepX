package `is`.hi.hbv501g.repx.exercises.web

import `is`.hi.hbv501g.repx.exercises.dto.CreateExerciseRequest
import `is`.hi.hbv501g.repx.exercises.dto.UpdateExerciseRequest
import `is`.hi.hbv501g.repx.exercises.dto.ExerciseDTO
import `is`.hi.hbv501g.repx.exercises.service.ExerciseService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/exercises")
class ExerciseController(
    private val service: ExerciseService
) {
    @PostMapping
    fun create(@Valid @RequestBody req: CreateExerciseRequest): ResponseEntity<ExerciseDTO> =
        ResponseEntity.ok(service.create(req))

    @GetMapping("/{id}")
    fun get(@PathVariable id: UUID): ResponseEntity<ExerciseDTO> =
        service.get(id)?.let { ResponseEntity.ok(it) } ?: ResponseEntity.notFound().build()

    @GetMapping
    fun list(pageable: Pageable): Page<ExerciseDTO> = service.list(pageable)

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: UUID,
        @Valid @RequestBody req: UpdateExerciseRequest
    ): ResponseEntity<ExerciseDTO> = ResponseEntity.ok(service.update(id, req))

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): ResponseEntity<Void> =
        if (service.delete(id)) ResponseEntity.noContent().build() else ResponseEntity.notFound().build()
}