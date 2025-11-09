package `is`.hi.hbv501g.repx.templates.service

import `is`.hi.hbv501g.repx.exercises.repo.ExerciseRepository
import `is`.hi.hbv501g.repx.templates.domain.Template
import `is`.hi.hbv501g.repx.templates.domain.TemplateExercise
import `is`.hi.hbv501g.repx.templates.dto.*
import `is`.hi.hbv501g.repx.templates.repo.TemplateExerciseRepository
import `is`.hi.hbv501g.repx.templates.repo.TemplateRepository
import `is`.hi.hbv501g.repx.users.repo.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.util.*

@Service
class TemplateService(
    private val templateRepo: TemplateRepository,
    private val templateExerciseRepo: TemplateExerciseRepository,
    private val userRepo: UserRepository,
    private val exerciseRepo: ExerciseRepository
) {

    @Transactional
    fun create(req: CreateTemplateRequest): TemplateDTO {
        val user = userRepo.findById(req.userId).orElseThrow { NoSuchElementException("user_not_found") }

        if (templateRepo.existsByUserIdAndNameIgnoreCase(user.id!!, req.name)) {
            throw IllegalArgumentException("template_name_taken")
        }

        val tpl = Template(
            user = user,
            name = req.name,
            notes = req.notes
        )

        req.exercises.forEach { line ->
            val ex = exerciseRepo.findById(line.exerciseId)
                .orElseThrow { NoSuchElementException("exercise_not_found") }

            tpl.exercises.add(
                TemplateExercise(
                    template = tpl,
                    exercise = ex,
                    orderIndex = line.orderIndex,
                    defaultReps = line.defaultReps,
                    defaultWeightKg = line.defaultWeightKg?.bd2()
                )
            )
        }

        return templateRepo.save(tpl).toDTO()
    }

    @Transactional(readOnly = true)
    fun get(id: UUID): TemplateDTO? =
        templateRepo.findById(id).orElse(null)?.toDTO()

    @Transactional(readOnly = true)
    fun list(pageable: Pageable): Page<TemplateDTO> =
        templateRepo.findAll(pageable).map { it.toDTO() }

    @Transactional(readOnly = true)
    fun listByUser(userId: UUID, pageable: Pageable): Page<TemplateDTO> =
        templateRepo.findAllByUserIdOrderByNameAsc(userId, pageable).map { it.toDTO() }

    @Transactional
    fun update(id: UUID, req: UpdateTemplateRequest): TemplateDTO {
        val tpl = templateRepo.findById(id).orElseThrow { NoSuchElementException("template_not_found") }

        req.name?.let { newName ->
            if (!newName.equals(tpl.name, ignoreCase = true) &&
                templateRepo.existsByUserIdAndNameIgnoreCaseAndIdNot(tpl.user.id!!, newName, tpl.id!!)
            ) {
                throw IllegalArgumentException("template_name_taken")
            }
            tpl.name = newName
        }

        req.notes?.let { tpl.notes = it }
        return templateRepo.save(tpl).toDTO()
    }

    @Transactional
    fun delete(id: UUID): Boolean =
        if (templateRepo.existsById(id)) { templateRepo.deleteById(id); true } else false

    @Transactional(readOnly = true)
    fun listExercises(templateId: UUID): List<TemplateExerciseDTO> {
        ensureTemplateExists(templateId)
        return templateExerciseRepo
            .findAllByTemplateIdOrderByOrderIndexAsc(templateId)
            .map { it.toDTO() }
    }

    @Transactional
    fun addExercise(templateId: UUID, req: CreateTemplateExerciseRequest): TemplateExerciseDTO {
        val tpl = templateRepo.findById(templateId).orElseThrow { NoSuchElementException("template_not_found") }
        val ex = exerciseRepo.findById(req.exerciseId).orElseThrow { NoSuchElementException("exercise_not_found") }

        val existing = templateExerciseRepo.findAllByTemplateIdOrderByOrderIndexAsc(templateId)
        val nextOrder = (existing.maxOfOrNull { it.orderIndex } ?: -1) + 1

        val desiredOrder = req.orderIndex.coerceAtLeast(0)

        val line = TemplateExercise(
            template = tpl,
            exercise = ex,
            orderIndex = desiredOrder,
            defaultReps = req.defaultReps,
            defaultWeightKg = req.defaultWeightKg?.bd2()
        )

        if (desiredOrder < nextOrder) {
            shiftDownFrom(templateId, desiredOrder)
        }

        return templateExerciseRepo.save(line).toDTO()
    }

    @Transactional
    fun updateExercise(templateId: UUID, lineId: UUID, req: UpdateTemplateExerciseRequest): TemplateExerciseDTO {
        val line = templateExerciseRepo.findByIdAndTemplateId(lineId, templateId)
            .orElseThrow { NoSuchElementException("template_exercise_not_found") }

        req.defaultReps?.let { line.defaultReps = it }
        req.defaultWeightKg?.let { line.defaultWeightKg = it.bd2() }

        if (req.orderIndex != null && req.orderIndex != line.orderIndex) {
            moveLineTo(templateId, line, req.orderIndex)
        }

        return templateExerciseRepo.save(line).toDTO()
    }

    @Transactional
    fun deleteExercise(templateId: UUID, lineId: UUID): Boolean {
        val lineOpt = templateExerciseRepo.findByIdAndTemplateId(lineId, templateId)
        if (lineOpt.isEmpty) return false
        val removed = lineOpt.get()
        val removedIndex = removed.orderIndex
        templateExerciseRepo.delete(removed)
        pullUpFrom(templateId, removedIndex + 1)
        return true
    }

    @Transactional
    fun reorderExercises(templateId: UUID, req: ReorderTemplateExercisesRequest): List<TemplateExerciseDTO> {
        val lines = templateExerciseRepo.findAllByTemplateIdOrderByOrderIndexAsc(templateId).toMutableList()
        val byId = lines.associateBy { it.id!! }.toMutableMap()

        if (req.orderedIds.size != lines.size || !req.orderedIds.all { byId.containsKey(it) }) {
            throw IllegalArgumentException("invalid_reorder_payload")
        }

        req.orderedIds.forEachIndexed { idx, id -> byId[id]!!.orderIndex = idx }
        templateExerciseRepo.saveAll(byId.values)

        return templateExerciseRepo.findAllByTemplateIdOrderByOrderIndexAsc(templateId).map { it.toDTO() }
    }

    private fun ensureTemplateExists(templateId: UUID) {
        if (!templateRepo.existsById(templateId)) throw NoSuchElementException("template_not_found")
    }

    private fun shiftDownFrom(templateId: UUID, fromIndex: Int) {
        val lines = templateExerciseRepo.findAllByTemplateIdOrderByOrderIndexAsc(templateId)
        lines.filter { it.orderIndex >= fromIndex }.forEach { it.orderIndex += 1 }
        templateExerciseRepo.saveAll(lines)
    }

    private fun pullUpFrom(templateId: UUID, fromIndex: Int) {
        val lines = templateExerciseRepo.findAllByTemplateIdOrderByOrderIndexAsc(templateId)
        lines.filter { it.orderIndex >= fromIndex }.forEach { it.orderIndex -= 1 }
        templateExerciseRepo.saveAll(lines)
    }

    private fun moveLineTo(templateId: UUID, line: TemplateExercise, newIndex: Int) {
        val lines = templateExerciseRepo.findAllByTemplateIdOrderByOrderIndexAsc(templateId).toMutableList()
        if (newIndex !in 0 until lines.size) throw IllegalArgumentException("order_index_out_of_range")

        lines.removeIf { it.id == line.id }
        lines.add(newIndex, line)
        lines.forEachIndexed { idx, l -> l.orderIndex = idx }
        templateExerciseRepo.saveAll(lines)
    }
}

private fun `is`.hi.hbv501g.repx.templates.domain.Template.toDTO(): TemplateDTO =
    TemplateDTO(
        id = this.id!!,
        userId = this.user.id!!,
        name = this.name,
        notes = this.notes,
        exercises = this.exercises
            .sortedBy { it.orderIndex }
            .map { it.toDTO() }
    )

private fun `is`.hi.hbv501g.repx.templates.domain.TemplateExercise.toDTO(): TemplateExerciseDTO =
    TemplateExerciseDTO(
        id = this.id!!,
        exerciseId = this.exercise.id!!,
        exerciseName = this.exercise.name,
        orderIndex = this.orderIndex,
        defaultReps = this.defaultReps,
        defaultWeightKg = this.defaultWeightKg?.toDouble()
    )

private fun Double.bd2(): BigDecimal =
    BigDecimal.valueOf(this).setScale(2)
