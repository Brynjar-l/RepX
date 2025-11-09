package `is`.hi.hbv501g.repx.workouts.repo

import `is`.hi.hbv501g.repx.workouts.domain.LiftSet
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface LiftSetRepository : JpaRepository<LiftSet, UUID> {
    fun findByWorkoutExercise_IdOrderBySetIndexAsc(workoutExerciseId: UUID): List<LiftSet>
}
