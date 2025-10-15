package `is`.hi.hbv501g.repx.workouts.web

import `is`.hi.hbv501g.repx.workouts.dto.CreateWorkoutRequest
import `is`.hi.hbv501g.repx.workouts.dto.WorkoutDTO
import `is`.hi.hbv501g.repx.workouts.service.WorkoutService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/workouts")
class WorkoutController(
    private val service: WorkoutService
) {
    @PostMapping
    fun create(@RequestBody req: CreateWorkoutRequest): ResponseEntity<WorkoutDTO> =
        ResponseEntity.ok(service.create(req))

    @GetMapping("/{id}")
    fun get(@PathVariable id: UUID): ResponseEntity<WorkoutDTO> =
        service.get(id)?.let { ResponseEntity.ok(it) } ?: ResponseEntity.notFound().build()

    @GetMapping
    fun list(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "startTime,desc") sort: String
    ): Page<WorkoutDTO> {
        val (prop, dir) = sort.split(",", limit = 2).let { it[0] to (it.getOrNull(1) ?: "asc") }
        val pageable: Pageable = PageRequest.of(
            page, size,
            if (dir.equals("desc", true)) Sort.by(Sort.Order.desc(prop)) else Sort.by(Sort.Order.asc(prop))
        )
        return service.list(pageable)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): ResponseEntity<Void> =
        if (service.delete(id)) ResponseEntity.noContent().build() else ResponseEntity.notFound().build()
}