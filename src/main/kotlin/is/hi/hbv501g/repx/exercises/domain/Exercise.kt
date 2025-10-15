package `is`.hi.hbv501g.repx.exercises.domain

import jakarta.persistence.*
import java.time.OffsetDateTime
import java.util.UUID

@Entity
@Table(name = "exercises", uniqueConstraints = [UniqueConstraint(columnNames = ["name"])])
class Exercise(
    @Id
    @Column(nullable = false, updatable = false)
    var id: UUID = UUID.randomUUID(),

    @Column(nullable = false, length = 120)
    var name: String,

    @Column(nullable = true, length = 60)
    var bodyPart: String? = null,

    @Column(nullable = true, length = 120)
    var equipment: String? = null,

    @Column(columnDefinition = "text")
    var description: String? = null,

    @Column(nullable = false)
    var createdAt: OffsetDateTime = OffsetDateTime.now()
)
