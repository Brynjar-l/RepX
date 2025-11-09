package `is`.hi.hbv501g.repx.users.service

import `is`.hi.hbv501g.repx.users.domain.User
import `is`.hi.hbv501g.repx.users.dto.CreateUserRequest
import `is`.hi.hbv501g.repx.users.dto.UserDTO
import `is`.hi.hbv501g.repx.users.repo.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.UUID

@Service
class UserService(private val repo: UserRepository) {
    private val encoder = BCryptPasswordEncoder()

    @Transactional
    fun createUser(req: CreateUserRequest): UserDTO {
        val user = User(
            email = req.email.trim().lowercase(),
            passwordHash = encoder.encode(req.password),
            displayName = req.displayName,
            createdAt = Instant.now()
        )
        return repo.save(user).toDTO()
    }

    @Transactional
    fun deleteUser(id: UUID): Boolean =
        repo.findById(id).map { repo.delete(it); true }.orElse(false)

    @Transactional(readOnly = true)
    fun getUser(id: UUID): UserDTO? =
        repo.findById(id).orElse(null)?.toDTO()

    @Transactional(readOnly = true)
    fun listUsers(pageable: Pageable): Page<UserDTO> =
        repo.findAll(pageable).map { it.toDTO() }
}

private fun User.toDTO() = UserDTO(
    id = this.id!!,
    email = this.email,
    displayName = this.displayName,
    createdAt = this.createdAt
)
