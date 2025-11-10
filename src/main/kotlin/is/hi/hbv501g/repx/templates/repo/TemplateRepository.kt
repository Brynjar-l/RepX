package `is`.hi.hbv501g.repx.templates.repo

import `is`.hi.hbv501g.repx.templates.domain.Template
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface TemplateRepository : JpaRepository<Template, UUID> {

    fun existsByUserIdAndNameIgnoreCase(userId: UUID, name: String): Boolean
    fun existsByUserIdAndNameIgnoreCaseAndIdNot(userId: UUID, name: String, id: UUID): Boolean

    @EntityGraph(attributePaths = ["user"], type = EntityGraphType.LOAD)
    fun findAllByUserIdOrderByNameAsc(userId: UUID, pageable: Pageable): Page<Template>

    @EntityGraph(attributePaths = ["user", "exercises", "exercises.exercise"], type = EntityGraphType.LOAD)
    override fun findById(id: UUID): Optional<Template>

    @EntityGraph(attributePaths = ["user"], type = EntityGraphType.LOAD)
    override fun findAll(pageable: Pageable): Page<Template>
}
