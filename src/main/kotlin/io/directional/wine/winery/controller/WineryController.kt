package io.directional.wine.winery.controller

import io.directional.wine.common.config.BasePayloads
import io.directional.wine.common.config.Uris
import io.directional.wine.common.response.BaseResponse
import io.directional.wine.common.response.ResponseMapper
import io.directional.wine.payload.winery.WineryPayloads
import io.directional.wine.winery.service.WineryService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class WineryController(
    private val responseMapper: ResponseMapper,
    private val wineryService: WineryService
) {

    @PostMapping(value = [Uris.WINERY_ROOT + "/list"])
    fun list(@Valid @RequestBody request: WineryPayloads.ListRequest): ResponseEntity<BaseResponse<Any?>> {
        val list: Page<BasePayloads.SimpleResponse> = wineryService.list(request)
        return responseMapper.ok(list)
    }

    @GetMapping(value = [Uris.WINERY_ROOT + Uris.REST_NAME_UUID])
    fun detail(@PathVariable uuid: String): ResponseEntity<BaseResponse<Any?>> {
        val detail: WineryPayloads.DetailResponse = wineryService.detail(uuid)
        return responseMapper.ok(detail)
    }

    @PostMapping(value = [Uris.WINERY_ROOT])
    fun create(@Valid @RequestBody request: WineryPayloads.CreateRequest): ResponseEntity<*> {
        wineryService.create(request)
        return responseMapper.ok()
    }

    @PatchMapping(value = [Uris.WINERY_ROOT])
    fun update(@Valid @RequestBody request: WineryPayloads.UpdateRequest) : ResponseEntity<*> {
        wineryService.update(request)
        return responseMapper.ok()
    }

    @DeleteMapping(value = [Uris.WINERY_ROOT + Uris.REST_NAME_UUID])
    fun delete(@PathVariable uuid:String) : ResponseEntity<*> {
        wineryService.delete(uuid)
        return responseMapper.ok()
    }

}
