package `is`.hi.hbv501g.repx.workouts.domain

import `is`.hi.hbv501g.repx.users.domain.User
import jakarta.persistence.*
import java.time.Instant
import java.util.*

@Entity
@Table(name = "workouts")
class Workout(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    @Column(nullable = false)
    var title: String,

    @Column(name = "start_time", nullable = false)
    var startTime: Instant,

    @Column(name = "end_time")
    var endTime: Instant? = null,

    var notes: String? = null,

    @OneToMany(mappedBy = "workout", cascade = [CascadeType.ALL], orphanRemoval = true)
    var exercises: MutableList<WorkoutExercise> = mutableListOf()
)
