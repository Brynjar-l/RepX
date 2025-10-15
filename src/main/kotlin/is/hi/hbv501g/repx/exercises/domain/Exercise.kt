package `is`.hi.hbv501g.repx.exercises.domain

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "exercises")
class Exercise(
    @Id
    @Column(columnDefinition = "uuid")
    var id: UUID? = null,

    @Column(nullable = false, unique = true)
    var name: String,

    @Column(name = "primary_muscle")
    var primaryMuscle: String? = null,

    var equipment: String? = null,

    var difficulty: String? = null,

    @Column(name = "is_public", nullable = false)
    var isPublic: Boolean = true
) {
    @PrePersist
    fun ensureId() { if (id == null) id = UUID.randomUUID() }
}