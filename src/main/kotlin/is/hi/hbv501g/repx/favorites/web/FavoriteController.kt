package `is`.hi.hbv501g.repx.favorites.web

import `is`.hi.hbv501g.repx.favorites.dto.AddFavoriteRequest
import `is`.hi.hbv501g.repx.favorites.dto.FavoriteDTO
import `is`.hi.hbv501g.repx.favorites.service.FavoriteService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
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
    fun list(
        @RequestParam userId: UUID,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "createdAt,desc") sort: String
    ): Page<FavoriteDTO> {
        val (prop, dir) = sort.split(",", limit = 2).let { it[0] to (it.getOrNull(1) ?: "desc") }
        val pageable: Pageable = PageRequest.of(
            page, size,
            if (dir.equals("desc", true)) Sort.by(Sort.Order.desc(prop)) else Sort.by(Sort.Order.asc(prop))
        )
        return service.listByUserPaged(userId, pageable)
    }

    @DeleteMapping
    fun delete(
        @RequestParam userId: UUID,
        @RequestParam exerciseId: UUID
    ): ResponseEntity<Void> =
        if (service.delete(userId, exerciseId)) ResponseEntity.noContent().build()
        else ResponseEntity.notFound().build()
}
