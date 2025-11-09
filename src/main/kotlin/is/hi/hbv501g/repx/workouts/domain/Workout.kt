package `is`.hi.hbv501g.repx.workouts.domain

import `is`.hi.hbv501g.repx.users.domain.User
import jakarta.persistence.*
import java.time.Instant
import java.util.*

@Entity
@Table(name = "workouts")
data class Workout(
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    val id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, columnDefinition = "uuid")
    val user: User,

    @Column(nullable = false)
    val title: String,

    @Column(name = "start_time", nullable = false)
    val startTime: Instant,

    @Column(name = "end_time")
    val endTime: Instant? = null,

    @Column(name = "notes")
    val notes: String? = null,

    @OneToMany(
        mappedBy = "workout",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    val exercises: MutableList<WorkoutExercise> = mutableListOf()
)