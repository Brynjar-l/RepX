package `is`.hi.hbv501g.repx.templates.domain

import jakarta.persistence.*
import java.math.BigDecimal
import java.util.UUID

@Entity
@Table(name = "template_exercises")
data class TemplateExercise(
    @Id
    @Column(columnDefinition = "uuid")
    val id: UUID? = null,

    @Column(name = "template_id", nullable = false, columnDefinition = "uuid")
    val templateId: UUID,

    @Column(name = "exercise_id", nullable = false, columnDefinition = "uuid")
    val exerciseId: UUID,

    @Column(name = "order_index", nullable = false)
    val orderIndex: Int = 0,

    @Column(name = "default_reps")
    val defaultReps: Int? = null,

    @Column(name = "default_weight_kg", columnDefinition = "numeric(6,2)")
    val defaultWeightKg: BigDecimal? = null
)
