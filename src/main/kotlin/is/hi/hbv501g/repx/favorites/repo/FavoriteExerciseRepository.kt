package `is`.hi.hbv501g.repx.favorites.repo

import `is`.hi.hbv501g.repx.favorites.domain.FavoriteExercise
import `is`.hi.hbv501g.repx.favorites.domain.FavoriteId
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface FavoriteExerciseRepository : JpaRepository<FavoriteExercise, FavoriteId> {
    fun findByIdUserIdOrderByCreatedAtDesc(userId: UUID): List<FavoriteExercise>
    fun existsByIdUserIdAndIdExerciseId(userId: UUID, exerciseId: UUID): Boolean
}
