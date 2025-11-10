package `is`.hi.hbv501g.repx.templates.web

import `is`.hi.hbv501g.repx.templates.dto.*
import `is`.hi.hbv501g.repx.templates.service.TemplateService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/templates/{templateId}/exercises")
class TemplateExerciseController(
    private val service: TemplateService
) {
    @GetMapping
    fun list(@PathVariable templateId: UUID): List<TemplateExerciseDTO> =
        service.listExercises(templateId)

    @PostMapping
    fun add(
        @PathVariable templateId: UUID,
        @RequestBody req: CreateTemplateExerciseRequest
    ): ResponseEntity<TemplateExerciseDTO> =
        ResponseEntity.ok(service.addExercise(templateId, req))

    @PutMapping("/{lineId}")
    fun update(
        @PathVariable templateId: UUID,
        @PathVariable lineId: UUID,
        @RequestBody req: UpdateTemplateExerciseRequest
    ): ResponseEntity<TemplateExerciseDTO> =
        ResponseEntity.ok(service.updateExercise(templateId, lineId, req))

    @DeleteMapping("/{lineId}")
    fun delete(
        @PathVariable templateId: UUID,
        @PathVariable lineId: UUID
    ): ResponseEntity<Void> =
        if (service.deleteExercise(templateId, lineId)) ResponseEntity.noContent().build()
        else ResponseEntity.notFound().build()

    @PostMapping("/reorder")
    fun reorder(
        @PathVariable templateId: UUID,
        @RequestBody req: ReorderTemplateExercisesRequest
    ): List<TemplateExerciseDTO> =
        service.reorderExercises(templateId, req)
}
