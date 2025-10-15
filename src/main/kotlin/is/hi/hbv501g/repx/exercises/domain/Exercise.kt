package `is`.hi.hbv501g.repx.exercises.domain

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "exercises")
class Exercise(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @Column(nullable = false, unique = true)
    var name: String,

    @Column(name = "primary_muscle")
    var primaryMuscle: String? = null,

    var equipment: String? = null,
    var difficulty: String? = null,

    @Column(name = "is_public", nullable = false)
    var isPublic: Boolean = true
)
