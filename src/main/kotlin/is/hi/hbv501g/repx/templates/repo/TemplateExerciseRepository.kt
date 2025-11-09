package `is`.hi.hbv501g.repx.templates.repo

import `is`.hi.hbv501g.repx.templates.domain.TemplateExercise
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface TemplateExerciseRepository : JpaRepository<TemplateExercise, UUID> {
    fun findByIdAndTemplateId(id: UUID, templateId: UUID): Optional<TemplateExercise>
    fun findAllByTemplateIdOrderByOrderIndexAsc(templateId: UUID): List<TemplateExercise>
}
