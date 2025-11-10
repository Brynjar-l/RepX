package `is`.hi.hbv501g.repx.favorites.domain

import `is`.hi.hbv501g.repx.exercises.domain.Exercise
import `is`.hi.hbv501g.repx.users.domain.User
import jakarta.persistence.*
import java.io.Serializable
import java.time.OffsetDateTime
import java.util.*

@Embeddable
data class FavoriteId(
    @Column(name = "user_id", columnDefinition = "uuid")
    val userId: UUID = UUID(0,0),
    @Column(name = "exercise_id", columnDefinition = "uuid")
    val exerciseId: UUID = UUID(0,0)
) : Serializable

@Entity
@Table(name = "favorite_exercises")
data class FavoriteExercise(
    @EmbeddedId
    val id: FavoriteId,

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id", columnDefinition = "uuid")
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("exerciseId")
    @JoinColumn(name = "exercise_id", columnDefinition = "uuid")
    val exercise: Exercise,

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    val createdAt: OffsetDateTime? = null
)
