package `is`.hi.hbv501g.repx.workouts.domain

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "sets")
class LiftSet(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "workout_exercise_id", nullable = false)
    var workoutExercise: WorkoutExercise,

    @Column(name = "set_index", nullable = false)
    var setIndex: Int,

    var reps: Int? = null,
    @Column(name = "weight_kg") var weightKg: Double? = null,
    var rir: Int? = null,
    @Column(name = "duration_sec") var durationSec: Int? = null,
    var notes: String? = null
)
