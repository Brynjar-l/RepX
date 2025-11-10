package `is`.hi.hbv501g.repx.workouts.repo

import `is`.hi.hbv501g.repx.workouts.domain.Workout
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface WorkoutRepository : JpaRepository<Workout, UUID> {

    @EntityGraph(attributePaths = ["exercises", "exercises.exercise", "exercises.sets"])
    override fun findAll(pageable: Pageable): Page<Workout>

    @EntityGraph(attributePaths = ["exercises", "exercises.exercise", "exercises.sets"])
    fun findByUser_Id(userId: UUID, pageable: Pageable): Page<Workout>
}
