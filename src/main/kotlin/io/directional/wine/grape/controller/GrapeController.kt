package io.directional.wine.grape.controller

import io.directional.wine.common.config.Uris
import io.directional.wine.common.response.BaseResponse
import io.directional.wine.common.response.ResponseMapper
import io.directional.wine.grape.service.GrapeService
import io.directional.wine.payload.grape.GrapePayloads
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class GrapeController(
    private val responseMapper: ResponseMapper,
    private val grapeService: GrapeService
) {

    @PostMapping(
        value = [Uris.GRAPE_ROOT + "/list"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun list(@Valid @RequestBody request: GrapePayloads.ListRequest): ResponseEntity<BaseResponse<Any?>> {
        val listResponse: Page<GrapePayloads.SimpleResponse> = grapeService.list(request)
        return responseMapper.ok(listResponse)
    }

    @GetMapping(value = [Uris.GRAPE_ROOT + Uris.REST_NAME_UUID], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun detail(@PathVariable uuid: String): ResponseEntity<BaseResponse<Any?>> {
        val detailResponse: GrapePayloads.DetailResponse = grapeService.detail(uuid)
        return responseMapper.ok(detailResponse)
    }

    @PostMapping(
        value = [Uris.GRAPE_ROOT],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun create(@Valid @RequestBody request: GrapePayloads.CreateRequest): ResponseEntity<*> {
        grapeService.create(request)
        return responseMapper.ok()
    }

    @PatchMapping(
        value = [Uris.GRAPE_ROOT],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun update(@Valid @RequestBody request: GrapePayloads.UpdateRequest): ResponseEntity<*> {
        grapeService.update(request)
        return responseMapper.ok()
    }

    @DeleteMapping(value = [Uris.GRAPE_ROOT + Uris.REST_NAME_UUID], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun delete(@PathVariable uuid: String): ResponseEntity<*> {
        grapeService.delete(uuid)
        return responseMapper.ok()
    }

    @PostMapping(
        value = [Uris.GRAPE_SHARE_ROOT],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun createGrapeShare(@Valid @RequestBody request : GrapePayloads.CreateShareRequest): ResponseEntity<*> {
        grapeService.createGrapeShare(request)
        return responseMapper.ok()
    }


}
