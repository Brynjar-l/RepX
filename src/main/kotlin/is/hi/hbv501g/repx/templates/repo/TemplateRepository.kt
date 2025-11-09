package `is`.hi.hbv501g.repx.templates.repo

import `is`.hi.hbv501g.repx.templates.domain.Template
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface TemplateRepository : JpaRepository<Template, UUID> {

    fun existsByUserIdAndNameIgnoreCase(userId: UUID, name: String): Boolean

    fun existsByUserIdAndNameIgnoreCaseAndIdNot(userId: UUID, name: String, id: UUID): Boolean

    fun findAllByUserIdOrderByNameAsc(userId: UUID, pageable: Pageable): Page<Template>
}
