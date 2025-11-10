package `is`.hi.hbv501g.repx.bodymetrics.service

import `is`.hi.hbv501g.repx.bodymetrics.domain.BodyMetric
import `is`.hi.hbv501g.repx.bodymetrics.dto.BodyMetricDTO
import `is`.hi.hbv501g.repx.bodymetrics.dto.CreateBodyMetricRequest
import `is`.hi.hbv501g.repx.bodymetrics.dto.UpdateBodyMetricRequest
import `is`.hi.hbv501g.repx.bodymetrics.repo.BodyMetricRepository
import `is`.hi.hbv501g.repx.users.repo.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*

@Service
class BodyMetricService(
    private val repo: BodyMetricRepository,
    private val userRepo: UserRepository
) {

    @Transactional
    fun create(req: CreateBodyMetricRequest): BodyMetricDTO {
        val user = userRepo.findById(req.userId).orElseThrow { NoSuchElementException("user_not_found") }

        val entity = BodyMetric(
            user = user,
            recordedAt = (req.recordedAt ?: Instant.now()).toUtcOffset(),
            weightKg = req.weightKg?.bd2(),
            bodyFatPct = req.bodyFatPct?.bd2(),
            chestCm = req.chestCm?.bd2(),
            waistCm = req.waistCm?.bd2(),
            hipsCm = req.hipsCm?.bd2()
        )
        return repo.save(entity).toDTO()
    }

    @Transactional(readOnly = true)
    fun get(id: UUID): BodyMetricDTO? =
        repo.findById(id).orElse(null)?.toDTO()

    @Transactional(readOnly = true)
    fun list(pageable: Pageable): Page<BodyMetricDTO> =
        repo.findAll(pageable).map { it.toDTO() }

    @Transactional(readOnly = true)
    fun listByUser(userId: UUID, pageable: Pageable): Page<BodyMetricDTO> =
        repo.findByUserId(userId, pageable).map { it.toDTO() }

    @Transactional(readOnly = true)
    fun listByUserAndRange(
        userId: UUID,
        fromInclusive: Instant?,
        toInclusive: Instant?,
        pageable: Pageable
    ): Page<BodyMetricDTO> {
        return when {
            fromInclusive != null && toInclusive != null ->
                repo.findByUserIdAndRecordedAtBetween(
                    userId, fromInclusive.toUtcOffset(), toInclusive.toUtcOffset(), pageable
                ).map { it.toDTO() }

            fromInclusive != null ->
                repo.findByUserIdAndRecordedAtGreaterThanEqual(
                    userId, fromInclusive.toUtcOffset(), pageable
                ).map { it.toDTO() }

            toInclusive != null ->
                repo.findByUserIdAndRecordedAtLessThanEqual(
                    userId, toInclusive.toUtcOffset(), pageable
                ).map { it.toDTO() }

            else -> listByUser(userId, pageable)
        }
    }

    @Transactional
    fun update(id: UUID, req: UpdateBodyMetricRequest): BodyMetricDTO {
        val entity = repo.findById(id).orElseThrow { NoSuchElementException("body_metric_not_found") }

        req.recordedAt?.let { entity.recordedAt = it.toUtcOffset() }
        req.weightKg?.let { entity.weightKg = it.bd2() }
        req.bodyFatPct?.let { entity.bodyFatPct = it.bd2() }
        req.chestCm?.let { entity.chestCm = it.bd2() }
        req.waistCm?.let { entity.waistCm = it.bd2() }
        req.hipsCm?.let { entity.hipsCm = it.bd2() }

        return repo.save(entity).toDTO()
    }

    @Transactional
    fun delete(id: UUID): Boolean =
        if (repo.existsById(id)) { repo.deleteById(id); true } else false
}

/* Helpers */

private fun Instant.toUtcOffset(): OffsetDateTime = this.atOffset(ZoneOffset.UTC)

private fun Double.bd2(): BigDecimal =
    BigDecimal.valueOf(this).setScale(2, RoundingMode.HALF_UP)

private fun `is`.hi.hbv501g.repx.bodymetrics.domain.BodyMetric.toDTO(): BodyMetricDTO =
    BodyMetricDTO(
        id = this.id!!,
        userId = this.user.id!!,
        recordedAt = this.recordedAt.toInstant(),
        weightKg = this.weightKg?.toDouble(),
        bodyFatPct = this.bodyFatPct?.toDouble(),
        chestCm = this.chestCm?.toDouble(),
        waistCm = this.waistCm?.toDouble(),
        hipsCm = this.hipsCm?.toDouble()
    )
