package `is`.hi.hbv501g.repx.workouts.domain

import `is`.hi.hbv501g.repx.exercises.domain.Exercise
import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import java.util.*

@Entity
@Table(name = "workout_exercises")
data class WorkoutExercise(
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(columnDefinition = "uuid")
    val id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "workout_id", nullable = false, columnDefinition = "uuid")
    val workout: Workout,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "exercise_id", nullable = false, columnDefinition = "uuid")
    val exercise: Exercise,

    @Column(name = "order_index", nullable = false)
    val orderIndex: Int = 0,

    @OneToMany(
        mappedBy = "workoutExercise",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    @OrderBy("setIndex ASC")
    val sets: MutableList<LiftSet> = mutableListOf()
)
