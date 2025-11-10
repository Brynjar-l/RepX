package `is`.hi.hbv501g.repx.bodymetrics.web

import `is`.hi.hbv501g.repx.bodymetrics.dto.*
import `is`.hi.hbv501g.repx.bodymetrics.service.BodyMetricService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.Instant
import java.util.*

@RestController
@RequestMapping("/body-metrics")
class BodyMetricController(
    private val service: BodyMetricService
) {

    @PostMapping
    fun create(@RequestBody req: CreateBodyMetricRequest): ResponseEntity<BodyMetricDTO> =
        ResponseEntity.ok(service.create(req))

    @GetMapping("/{id}")
    fun get(@PathVariable id: UUID): ResponseEntity<BodyMetricDTO> =
        service.get(id)?.let { ResponseEntity.ok(it) } ?: ResponseEntity.notFound().build()

    @GetMapping
    fun list(
        @RequestParam(required = false) userId: UUID?,
        @RequestParam(required = false) from: Instant?,
        @RequestParam(required = false) to: Instant?,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "recordedAt,desc") sort: String
    ): Page<BodyMetricDTO> {
        val (prop, dir) = sort.split(",", limit = 2).let { it[0] to (it.getOrNull(1) ?: "asc") }
        val pageable: Pageable = PageRequest.of(
            page, size,
            if (dir.equals("desc", true)) Sort.by(Sort.Order.desc(prop)) else Sort.by(Sort.Order.asc(prop))
        )
        return if (userId != null)
            service.listByUserAndRange(userId, from, to, pageable)
        else
            service.list(pageable)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: UUID, @RequestBody req: UpdateBodyMetricRequest): ResponseEntity<BodyMetricDTO> =
        ResponseEntity.ok(service.update(id, req))

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): ResponseEntity<Void> =
        if (service.delete(id)) ResponseEntity.noContent().build() else ResponseEntity.notFound().build()
}
