package `is`.hi.hbv501g.repx.favorites.repo

import `is`.hi.hbv501g.repx.favorites.domain.FavoriteExercise
import `is`.hi.hbv501g.repx.favorites.domain.FavoriteId
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface FavoriteExerciseRepository : JpaRepository<FavoriteExercise, FavoriteId> {

    @EntityGraph(attributePaths = ["exercise"])
    @Query(
        "select f from FavoriteExercise f " +
        "where f.id.userId = :userId " +
        "order by f.createdAt desc"
    )
    fun findAllByUserIdOrderByCreatedAtDesc(userId: UUID): List<FavoriteExercise>

    fun existsByIdUserIdAndIdExerciseId(userId: UUID, exerciseId: UUID): Boolean
}
