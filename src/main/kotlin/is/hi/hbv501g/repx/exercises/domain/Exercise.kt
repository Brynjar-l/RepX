package `is`.hi.hbv501g.repx.exercises.domain

import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import java.util.UUID

@Entity
@Table(name = "exercises")
data class Exercise(
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(columnDefinition = "uuid")
    val id: UUID? = null,

    @Column(nullable = false, unique = true)
    var name: String,

    @Column(name = "primary_muscle")
    var primaryMuscle: String? = null,

    @Column
    var equipment: String? = null,

    @Column
    var difficulty: String? = null,

    @Column(name = "is_public", nullable = false)
    var isPublic: Boolean = true
)
