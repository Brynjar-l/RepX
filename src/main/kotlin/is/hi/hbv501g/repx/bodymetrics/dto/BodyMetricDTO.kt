package `is`.hi.hbv501g.repx.bodymetrics.dto

import java.time.Instant
import java.util.*

data class CreateBodyMetricRequest(
    val userId: UUID,
    val recordedAt: Instant? = null,
    val weightKg: Double? = null,
    val bodyFatPct: Double? = null,
    val chestCm: Double? = null,
    val waistCm: Double? = null,
    val hipsCm: Double? = null
)

data class UpdateBodyMetricRequest(
    val recordedAt: Instant? = null,
    val weightKg: Double? = null,
    val bodyFatPct: Double? = null,
    val chestCm: Double? = null,
    val waistCm: Double? = null,
    val hipsCm: Double? = null
)

data class BodyMetricDTO(
    val id: UUID,
    val userId: UUID,
    val recordedAt: Instant,
    val weightKg: Double?,
    val bodyFatPct: Double?,
    val chestCm: Double?,
    val waistCm: Double?,
    val hipsCm: Double?
)
