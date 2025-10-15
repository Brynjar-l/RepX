package `is`.hi.hbv501g.repx.workouts.domain

import `is`.hi.hbv501g.repx.exercises.domain.Exercise
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "workout_exercises")
class WorkoutExercise(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "workout_id", nullable = false)
    var workout: Workout,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "exercise_id", nullable = false)
    var exercise: Exercise,

    @Column(name = "order_index", nullable = false)
    var orderIndex: Int = 0,

    @OneToMany(mappedBy = "workoutExercise", cascade = [CascadeType.ALL], orphanRemoval = true)
    var sets: MutableList<LiftSet> = mutableListOf()
)
