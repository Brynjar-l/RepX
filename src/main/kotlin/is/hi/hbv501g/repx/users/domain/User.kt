package `is`.hi.hbv501g.repx.users.domain

import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.annotations.UuidGenerator
import org.hibernate.type.SqlTypes
import java.time.OffsetDateTime
import java.util.UUID

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(columnDefinition = "uuid")
    val id: UUID? = null,

    @Column(name = "email", nullable = false, unique = true, columnDefinition = "citext")
    @JdbcTypeCode(SqlTypes.OTHER)
    val email: String,

    @Column(name = "password_hash", nullable = false)
    val passwordHash: String,

    @Column(name = "display_name", nullable = false)
    val displayName: String,

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    val createdAt: OffsetDateTime? = null
)
