package `is`.hi.hbv501g.repx.workouts.repo

import `is`.hi.hbv501g.repx.workouts.domain.Workout
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.PagingAndSortingRepository
import java.util.UUID

interface WorkoutRepository :
    JpaRepository<Workout, UUID>,
    PagingAndSortingRepository<Workout, UUID>
