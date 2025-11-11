package `is`.hi.hbv501g.repx.favorites.service

import `is`.hi.hbv501g.repx.favorites.domain.FavoriteExercise
import `is`.hi.hbv501g.repx.favorites.domain.FavoriteId
import `is`.hi.hbv501g.repx.favorites.dto.AddFavoriteRequest
import `is`.hi.hbv501g.repx.favorites.dto.FavoriteDTO
import `is`.hi.hbv501g.repx.favorites.repo.FavoriteExerciseRepository
import `is`.hi.hbv501g.repx.users.repo.UserRepository
import `is`.hi.hbv501g.repx.exercises.repo.ExerciseRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class FavoriteService(
    private val repo: FavoriteExerciseRepository,
    private val userRepo: UserRepository,
    private val exerciseRepo: ExerciseRepository
) {

    @Transactional
    fun add(req: AddFavoriteRequest): FavoriteDTO {
        val user = userRepo.findById(req.userId)
            .orElseThrow { NoSuchElementException("user_not_found") }
        val exercise = exerciseRepo.findById(req.exerciseId)
            .orElseThrow { NoSuchElementException("exercise_not_found") }

        val key = FavoriteId(user.id!!, exercise.id!!)
        val existing = repo.findById(key)
        if (existing.isPresent) return existing.get().toDTO()

        val entity = FavoriteExercise(
            id = key,
            user = user,
            exercise = exercise
        )
        return repo.save(entity).toDTO()
    }

    @Transactional(readOnly = true)
    fun listByUser(userId: UUID): List<FavoriteDTO> {
        if (!userRepo.existsById(userId)) throw NoSuchElementException("user_not_found")
        return repo.findByIdUserIdOrderByCreatedAtDesc(userId).map { it.toDTO() }
    }

    @Transactional(readOnly = true)
    fun listByUserPaged(userId: UUID, pageable: Pageable): Page<FavoriteDTO> {
        if (!userRepo.existsById(userId)) throw NoSuchElementException("user_not_found")
        return repo.findByIdUserIdOrderByCreatedAtDesc(userId, pageable).map { it.toDTO() }
    }

    @Transactional
    fun delete(userId: UUID, exerciseId: UUID): Boolean {
        val key = FavoriteId(userId, exerciseId)
        val exists = repo.existsById(key)
        if (exists) repo.deleteById(key)
        return exists
    }
}

private fun `is`.hi.hbv501g.repx.favorites.domain.FavoriteExercise.toDTO() = FavoriteDTO(
    userId = this.user.id!!,
    exerciseId = this.exercise.id!!,
    exerciseName = this.exercise.name,
    createdAt = this.createdAt
)
