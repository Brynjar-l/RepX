package `is`.hi.hbv501g.repx.users.domain

import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.UUID

@Entity
@Table(name = "users")
data class User(
    @Id
    @Column(columnDefinition = "uuid")
    val id: UUID? = null,

    @Column(name = "email", nullable = false, unique = true, columnDefinition = "citext")
    @JdbcTypeCode(SqlTypes.OTHER)
    val email: String,

    @Column(name = "password_hash", nullable = false)
    val passwordHash: String,

    @Column(name = "display_name", nullable = false)
    val displayName: String,

    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: OffsetDateTime = OffsetDateTime.now(ZoneOffset.UTC)
) {
    @PrePersist
    fun onCreate() {
        if (createdAt == null) { 
            createdAt = OffsetDateTime.now(ZoneOffset.UTC)
        }
    }
}
