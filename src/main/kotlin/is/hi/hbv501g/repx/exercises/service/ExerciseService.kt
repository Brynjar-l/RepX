package `is`.hi.hbv501g.repx.exercises.service

import `is`.hi.hbv501g.repx.exercises.domain.Exercise
import `is`.hi.hbv501g.repx.exercises.dto.CreateExerciseRequest
import `is`.hi.hbv501g.repx.exercises.dto.ExerciseDTO
import `is`.hi.hbv501g.repx.exercises.dto.UpdateExerciseRequest
import `is`.hi.hbv501g.repx.exercises.repo.ExerciseRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class ExerciseService(
    private val repo: ExerciseRepository
) {

    @Transactional
    fun create(req: CreateExerciseRequest): ExerciseDTO {
        if (repo.existsByNameIgnoreCase(req.name)) {
            throw IllegalArgumentException("exercise_name_taken")
        }
        val entity = Exercise(
            name = req.name,
            primaryMuscle = req.primaryMuscle,
            equipment = req.equipment,
            difficulty = req.difficulty,
            isPublic = req.isPublic
        )
        return repo.save(entity).toDTO()
    }

    @Transactional(readOnly = true)
    fun get(id: UUID): ExerciseDTO? =
        repo.findById(id).orElse(null)?.toDTO()

    @Transactional(readOnly = true)
    fun list(pageable: Pageable): Page<ExerciseDTO> =
        repo.findAll(pageable).map { it.toDTO() }

    @Transactional
    fun update(id: UUID, req: UpdateExerciseRequest): ExerciseDTO {
        val entity = repo.findById(id).orElseThrow { NoSuchElementException("exercise_not_found") }

        req.name?.let { newName ->
            if (!newName.equals(entity.name, ignoreCase = true) && repo.existsByNameIgnoreCase(newName)) {
                throw IllegalArgumentException("exercise_name_taken")
            }
            entity.name = newName
        }
        req.primaryMuscle?.let { entity.primaryMuscle = it }
        req.equipment?.let { entity.equipment = it }
        req.difficulty?.let { entity.difficulty = it }
        req.isPublic?.let { entity.isPublic = it }

        return repo.save(entity).toDTO()
    }

    @Transactional
    fun delete(id: UUID): Boolean =
        if (repo.existsById(id)) { repo.deleteById(id); true } else false
}

private fun Exercise.toDTO() = ExerciseDTO(
    id = this.id!!,
    name = this.name,
    primaryMuscle = this.primaryMuscle,
    equipment = this.equipment,
    difficulty = this.difficulty,
    isPublic = this.isPublic
)
