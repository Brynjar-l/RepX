package `is`.hi.hbv501g.repx

import `is`.hi.hbv501g.repx.CreateUserRequest
import `is`.hi.hbv501g.repx.User
import `is`.hi.hbv501g.repx.UserDTO
import `is`.hi.hbv501g.repx.UserRepository
import `is`.hi.hbv501g.repx.toDTO
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.UUID

@Service
class UserService(private val repo: UserRepository) {
    private val encoder = BCryptPasswordEncoder()
    private val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    @Transactional
    fun createUser(req: CreateUserRequest): UserDTO {
        val email = req.email.trim().lowercase()
        require(emailRegex.matches(email)) { "invalid_email" }
        require(req.password.length >= 6) { "password_too_short" }
        if (repo.existsByEmailIgnoreCase(email)) error("email_taken")

        val user = User(
            email = email,
            passwordHash = encoder.encode(req.password),
            displayName = req.displayName.ifBlank { email.substringBefore("@") },
            createdAt = Instant.now()
        )
        return repo.save(user).toDTO()
    }

    @Transactional
    fun deleteUser(id: UUID): Boolean {
        if (!repo.existsById(id)) return false
        repo.deleteById(id); return true
    }
}
