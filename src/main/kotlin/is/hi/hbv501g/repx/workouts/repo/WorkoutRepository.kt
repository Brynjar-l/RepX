package `is`.hi.hbv501g.repx.workouts.repo

import `is`.hi.hbv501g.repx.workouts.domain.Workout
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.Instant
import java.util.*

interface WorkoutRepository : JpaRepository<Workout, UUID> {

    fun findByUserIdOrderByStartTimeDesc(
        userId: UUID,
        pageable: Pageable
    ): Page<Workout>

    fun findByUserIdAndStartTimeBetween(
        userId: UUID,
        startTime: Instant,
        endTime: Instant
    ): List<Workout>

    @Query(
        """
        select w from Workout w
        join w.exercises we
        where we.exercise.id = :exerciseId
          and (:userId is null or w.user.id = :userId)
        order by w.startTime desc
        """
    )
    fun findHistoryForExercise(
        @Param("exerciseId") exerciseId: UUID,
        @Param("userId") userId: UUID?,
        pageable: Pageable
    ): Page<Workout>

    @Query(
        """
        select we.exercise.id, we.exercise.name, s.weightKg, s.reps
        from Workout w
        join w.exercises we
        join we.sets s
        where w.user.id = :userId
          and s.weightKg is not null
        """
    )
    fun allSetsForUser(
        @Param("userId") userId: UUID
    ): List<Array<Any?>>
}
