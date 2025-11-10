package `is`.hi.hbv501g.repx.favorites.web

import `is`.hi.hbv501g.repx.favorites.dto.AddFavoriteRequest
import `is`.hi.hbv501g.repx.favorites.dto.FavoriteDTO
import `is`.hi.hbv501g.repx.favorites.service.FavoriteService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/favorites")
class FavoriteController(
    private val service: FavoriteService
) {
    @PostMapping
    fun add(@RequestBody req: AddFavoriteRequest): ResponseEntity<FavoriteDTO> =
        ResponseEntity.ok(service.add(req))

    @GetMapping
    fun list(@RequestParam userId: UUID): List<FavoriteDTO> =
        service.listByUser(userId)

    @DeleteMapping
    fun delete(
        @RequestParam userId: UUID,
        @RequestParam exerciseId: UUID
    ): ResponseEntity<Void> =
        if (service.delete(userId, exerciseId)) ResponseEntity.noContent().build()
        else ResponseEntity.notFound().build()
}
