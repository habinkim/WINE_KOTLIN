package io.directional.wine.wine.controller

import io.directional.wine.common.config.Uris
import io.directional.wine.common.response.BaseResponse
import io.directional.wine.common.response.ResponseMapper
import io.directional.wine.payload.wine.ImporterPayloads
import io.directional.wine.payload.wine.WinePayloads
import io.directional.wine.wine.service.WineService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class WineController(
    private val responseMapper: ResponseMapper,
    private val wineService: WineService
) {

    @GetMapping(value = [Uris.IMPORTER_ROOT])
    fun importerList(@RequestParam(required = false) name: String?): ResponseEntity<BaseResponse<Any?>> {
        val list: List<ImporterPayloads.ListResponse> = wineService.importerList(name)
        return responseMapper.ok(list)
    }

    @GetMapping(value = [Uris.IMPORTER_ROOT + Uris.REST_NAME_UUID])
    fun importerDetail(@PathVariable uuid: String): ResponseEntity<BaseResponse<Any?>> {
        val detail: ImporterPayloads.DetailResponse = wineService.importerDetail(uuid)
        return responseMapper.ok(detail)
    }

    @PostMapping(value = [Uris.IMPORTER_ROOT])
    fun createImporter(@Valid @RequestBody request: ImporterPayloads.CreateRequest): ResponseEntity<*> {
        wineService.createImporter(request)
        return responseMapper.ok()
    }

    @PatchMapping(value = [Uris.IMPORTER_ROOT])
    fun updateImporter(@Valid @RequestBody request: ImporterPayloads.UpdateRequest) : ResponseEntity<*> {
        wineService.updateImporter(request)
        return responseMapper.ok()
    }

    @DeleteMapping(value = [Uris.IMPORTER_ROOT + Uris.REST_NAME_UUID])
    fun deleteImporter(@PathVariable uuid:String) : ResponseEntity<*> {
        wineService.deleteImporter(uuid)
        return responseMapper.ok()
    }


    @PostMapping(value = [Uris.WINE_ROOT + "/list"])
    fun wineList(@Valid @RequestBody request: WinePayloads.ListRequest): ResponseEntity<BaseResponse<Any?>> {
        val list: Page<WinePayloads.ListResponse> = wineService.wineList(request)
        return responseMapper.ok(list)
    }

    @GetMapping(value = [Uris.WINE_ROOT + Uris.REST_NAME_UUID])
    fun wineDetail(@PathVariable uuid: String): ResponseEntity<BaseResponse<Any?>> {
        val detail: WinePayloads.DetailResponse = wineService.wineDetail(uuid)
        return responseMapper.ok(detail)
    }

    @PostMapping(value = [Uris.WINE_ROOT])
    fun createWine(@Valid @RequestBody request: WinePayloads.CreateRequest): ResponseEntity<*> {
        wineService.createWine(request)
        return responseMapper.ok()
    }

    @PatchMapping(value = [Uris.WINE_ROOT])
    fun updateWine(@Valid @RequestBody request: WinePayloads.UpdateRequest) : ResponseEntity<*> {
        wineService.updateWine(request)
        return responseMapper.ok()
    }

    @DeleteMapping(value = [Uris.WINE_ROOT + Uris.REST_NAME_UUID])
    fun deleteWine(@PathVariable uuid:String) : ResponseEntity<*> {
        wineService.deleteWine(uuid)
        return responseMapper.ok()
    }

}
