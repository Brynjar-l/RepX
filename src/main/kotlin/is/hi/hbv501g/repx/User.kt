package is.hi.hbv501g.repx

import jakarta.persistence.*
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "users")
data class User(
    @Id @GeneratedValue var id: UUID? = null,
    @Column(nullable = false, unique = true) var email: String = "",
    @Column(name = "password_hash", nullable = false) var passwordHash: String = "",
    @Column(name = "display_name", nullable = false) var displayName: String = "",
    @Column(name = "created_at", nullable = false) var createdAt: Instant = Instant.now()
)
