package `is`.hi.hbv501g.repx.exercises.repo

import `is`.hi.hbv501g.repx.exercises.domain.Exercise
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ExerciseRepository : JpaRepository<Exercise, UUID>
