package `is`.hi.hbv501g.repx.users.service

import `is`.hi.hbv501g.repx.users.domain.User
import `is`.hi.hbv501g.repx.users.dto.CreateUserRequest
import `is`.hi.hbv501g.repx.users.dto.UserDTO
import `is`.hi.hbv501g.repx.users.repo.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.*
import hi.hbv501g.repx.users.dto.UpdateUserRequest

@Service
class UserService(private val repo: UserRepository) {
    private val encoder = BCryptPasswordEncoder()

    fun createUser(req: CreateUserRequest): UserDTO {
        if (repo.existsByEmail(req.email.trim())) {
            throw IllegalArgumentException("email_taken")
        }

        val user = User(
            email = req.email.trim(),
            passwordHash = encoder.encode(req.password),
            displayName = req.displayName.trim()
        )
        val saved = repo.save(user)
        val reloaded = repo.findById(saved.id!!).orElse(saved)

        return reloaded.toDTO()
    }

    fun updateUser(id: UUID, req: UpdateUserRequest): UserDTO {
    val user = repo.findById(id).orElseThrow { IllegalArgumentException("user_not_found") }

    val newEmail = req.email?.trim()?.lowercase()
    val newName = req.displayName?.trim()

    if (newEmail != null && newEmail != user.email) {
        if (repo.existsByEmail(newEmail)) {
            throw IllegalArgumentException("email_taken")
        }
        user.copy(email = newEmail)
    }

    val updated = user.copy(
        email = newEmail ?: user.email,
        displayName = newName ?: user.displayName
    )

    val saved = repo.save(updated)
    return saved.toDTO()
}

    fun deleteUser(id: UUID): Boolean =
        repo.findById(id).map { repo.delete(it); true }.orElse(false)

    fun getUser(id: UUID): UserDTO? =
        repo.findById(id).orElse(null)?.toDTO()

    fun listUsers(pageable: Pageable): Page<UserDTO> =
        repo.findAll(pageable).map { it.toDTO() }
}

private fun User.toDTO() = UserDTO(
    id = this.id!!,
    email = this.email,
    displayName = this.displayName,
    createdAt = this.createdAt!!
)
