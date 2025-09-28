package `is`.hi.hbv501g.repx

import `is`.hi.hbv501g.repx.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/users")
class UserController(private val service: UserService) {

    @PostMapping
    fun create(@RequestBody req: CreateUserRequest): ResponseEntity<Any> =
        runCatching { service.createUser(req) }.fold(
            onSuccess = { ResponseEntity.status(HttpStatus.CREATED).body(it) },
            onFailure = { e ->
                when (e.message) {
                    "invalid_email" -> ResponseEntity.badRequest().body(mapOf("error" to "invalid_email"))
                    "password_too_short" -> ResponseEntity.badRequest().body(mapOf("error" to "password_too_short"))
                    "email_taken" -> ResponseEntity.status(HttpStatus.CONFLICT).body(mapOf("error" to "email_taken"))
                    else -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mapOf("error" to "server_error"))
                }
            }
        )

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): ResponseEntity<Any> =
        if (service.deleteUser(id)) ResponseEntity.noContent().build()
        else ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("error" to "user_not_found"))
}
