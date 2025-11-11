package `is`.hi.hbv501g.repx.workouts.domain

import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import java.math.BigDecimal
import java.util.*

@Entity
@Table(name = "sets")
data class LiftSet(
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(columnDefinition = "uuid")
    val id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "workout_exercise_id", nullable = false, columnDefinition = "uuid")
    val workoutExercise: WorkoutExercise,

    @Column(name = "set_index", nullable = false)
    val setIndex: Int = 1,

    @Column(name = "reps")
    val reps: Int? = null,

    @Column(name = "weight_kg", columnDefinition = "numeric(6,2)")
    val weightKg: BigDecimal? = null,

    @Column(name = "rir")
    val rir: Int? = null,

    @Column(name = "duration_sec")
    val durationSec: Int? = null,

    @Column(name = "notes")
    val notes: String? = null
)
