package `is`.hi.hbv501g.repx.users.web

import `is`.hi.hbv501g.repx.users.dto.CreateUserRequest
import `is`.hi.hbv501g.repx.users.dto.UserDTO
import `is`.hi.hbv501g.repx.users.service.UserService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/users")
class UserController(private val service: UserService) {

    @PostMapping
    fun create(@RequestBody req: CreateUserRequest): ResponseEntity<UserDTO> =
        ResponseEntity.ok(service.createUser(req))

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): ResponseEntity<Void> =
        if (service.deleteUser(id)) ResponseEntity.noContent().build()
        else ResponseEntity.notFound().build()

    @GetMapping("/{id}")
    fun get(@PathVariable id: UUID): ResponseEntity<UserDTO> =
        service.getUser(id)?.let { ResponseEntity.ok(it) } ?: ResponseEntity.notFound().build()

    @GetMapping
    fun list(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "createdAt,desc") sort: String
    ): Page<UserDTO> {
        val (prop, dir) = sort.split(",", limit = 2).let { it[0] to (it.getOrNull(1) ?: "asc") }
        val pageable: Pageable = PageRequest.of(
            page, size,
            if (dir.equals("desc", true)) Sort.by(Sort.Order.desc(prop)) else Sort.by(Sort.Order.asc(prop))
        )
        return service.listUsers(pageable)
    }
}
