package `is`.hi.hbv501g.repx.workouts.repo

import `is`.hi.hbv501g.repx.workouts.domain.Workout
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface WorkoutRepository : JpaRepository<Workout, UUID> {
    fun findByUser_Id(userId: UUID, pageable: Pageable): Page<Workout>
}