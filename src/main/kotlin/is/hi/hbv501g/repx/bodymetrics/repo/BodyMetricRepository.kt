package `is`.hi.hbv501g.repx.bodymetrics.repo

import `is`.hi.hbv501g.repx.bodymetrics.domain.BodyMetric
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.time.OffsetDateTime
import java.util.*

interface BodyMetricRepository : JpaRepository<BodyMetric, UUID> {

    fun findByUser_Id(userId: UUID, pageable: Pageable): Page<BodyMetric>

    fun findByUser_IdAndRecordedAtBetween(
        userId: UUID,
        from: OffsetDateTime,
        to: OffsetDateTime,
        pageable: Pageable
    ): Page<BodyMetric>

    fun findByUser_IdAndRecordedAtGreaterThanEqual(
        userId: UUID,
        from: OffsetDateTime,
        pageable: Pageable
    ): Page<BodyMetric>

    fun findByUser_IdAndRecordedAtLessThanEqual(
        userId: UUID,
        to: OffsetDateTime,
        pageable: Pageable
    ): Page<BodyMetric>
}
