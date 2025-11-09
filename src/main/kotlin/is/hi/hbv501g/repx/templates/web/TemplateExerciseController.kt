package `is`.hi.hbv501g.repx.templates.web

import `is`.hi.hbv501g.repx.templates.dto.CreateTemplateExerciseRequest
import `is`.hi.hbv501g.repx.templates.dto.TemplateExerciseDTO
import `is`.hi.hbv501g.repx.templates.dto.UpdateTemplateExerciseRequest
import `is`.hi.hbv501g.repx.templates.dto.ReorderTemplateExercisesRequest
import `is`.hi.hbv501g.repx.templates.service.TemplateService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/templates/{templateId}/exercises")
class TemplateExerciseController(
    private val service: TemplateService
) {

    @GetMapping
    fun list(
        @PathVariable templateId: UUID
    ): List<TemplateExerciseDTO> = service.listExercises(templateId)

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
    ): List<TemplateExerciseDTO> = service.reorderExercises(templateId, req)
}
