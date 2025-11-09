package `is`.hi.hbv501g.repx.templates.domain

import `is`.hi.hbv501g.repx.exercises.domain.Exercise
import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import java.math.BigDecimal
import java.util.UUID

@Entity
@Table(name = "template_exercises")
data class TemplateExercise(
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(columnDefinition = "uuid")
    val id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "template_id", nullable = false, columnDefinition = "uuid")
    val template: Template,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "exercise_id", nullable = false, columnDefinition = "uuid")
    val exercise: Exercise,

    @Column(name = "order_index", nullable = false)
    var orderIndex: Int = 0,

    @Column(name = "default_reps")
    var defaultReps: Int? = null,

    @Column(name = "default_weight_kg", columnDefinition = "numeric(6,2)")
    var defaultWeightKg: BigDecimal? = null
)
