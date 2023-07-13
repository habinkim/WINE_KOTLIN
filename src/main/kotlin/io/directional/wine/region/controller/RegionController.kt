package io.directional.wine.region.controller

import io.directional.wine.common.config.BasePayloads
import io.directional.wine.common.config.Uris
import io.directional.wine.common.response.BaseResponse
import io.directional.wine.common.response.ResponseMapper
import io.directional.wine.payload.region.RegionPayloads
import io.directional.wine.region.service.RegionService
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.data.domain.Page
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class RegionController(
    private val responseMapper: ResponseMapper,
    private val regionService: RegionService
) {

    @PostMapping(
        value = [Uris.REGION_ROOT + "/list"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun list(@Valid @RequestBody request: RegionPayloads.ListRequest): ResponseEntity<BaseResponse<Any?>> {
        val simpleResponse: Page<BasePayloads.SimpleResponse> = regionService.list(request)
        return responseMapper.ok(simpleResponse)
    }

    @GetMapping(value = [Uris.REGION_ROOT + Uris.REST_NAME_UUID], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun detail(@NotBlank @PathVariable(name = "uuid") uuid: String): ResponseEntity<BaseResponse<Any?>> {
        val detailResponse: RegionPayloads.DetailResponse = regionService.detail(uuid)
        return responseMapper.ok(detailResponse)
    }

    @PostMapping(
        value = [Uris.REGION_ROOT],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun create(@Valid @RequestBody request: RegionPayloads.CreateRequest): ResponseEntity<*> {
        regionService.create(request)
        return responseMapper.ok()
    }

    @PatchMapping(
        value = [Uris.REGION_ROOT],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun update(@Valid @RequestBody request: RegionPayloads.UpdateRequest): ResponseEntity<*> {
        regionService.update(request)
        return responseMapper.ok()
    }

    @DeleteMapping(value = [Uris.REGION_ROOT + Uris.REST_NAME_UUID], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun delete(@NotBlank @PathVariable(name = "uuid") uuid: String): ResponseEntity<*> {
        regionService.delete(uuid)
        return responseMapper.ok()
    }
}
