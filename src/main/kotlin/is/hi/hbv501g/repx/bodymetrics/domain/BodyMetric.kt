package `is`.hi.hbv501g.repx.bodymetrics.domain

import `is`.hi.hbv501g.repx.users.domain.User
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.*

@Entity
@Table(name = "body_metrics")
data class BodyMetric(
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    val id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, columnDefinition = "uuid")
    val user: User,

    @Column(name = "recorded_at", nullable = false)
    var recordedAt: OffsetDateTime,

    @Column(name = "weight_kg", columnDefinition = "numeric(6,2)")
    var weightKg: BigDecimal? = null,

    @Column(name = "body_fat_pct", columnDefinition = "numeric(5,2)")
    var bodyFatPct: BigDecimal? = null,

    @Column(name = "chest_cm", columnDefinition = "numeric(6,2)")
    var chestCm: BigDecimal? = null,

    @Column(name = "waist_cm", columnDefinition = "numeric(6,2)")
    var waistCm: BigDecimal? = null,

    @Column(name = "hips_cm", columnDefinition = "numeric(6,2)")
    var hipsCm: BigDecimal? = null
)
