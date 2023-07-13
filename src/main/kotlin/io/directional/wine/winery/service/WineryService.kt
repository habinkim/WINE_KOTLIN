package io.directional.wine.winery.service

import io.directional.wine.common.config.BasePayloads
import io.directional.wine.common.config.logger
import io.directional.wine.common.util.StringUtils
import io.directional.wine.domain.winery.Winery
import io.directional.wine.payload.winery.WineryPayloads
import io.directional.wine.wine.service.WineGateway
import io.directional.wine.winery.mapper.WineryMapper
import io.directional.wine.winery.repository.WineryRepository
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class WineryService(
    private val wineryRepository: WineryRepository,

    private val wineryGateway: WineryGateway,
    private val wineGateway: WineGateway,

    private val mapper: WineryMapper,

    private val stringUtils: StringUtils
) {

    private val log = logger()

    @Transactional(readOnly = true)
    fun list(request: WineryPayloads.ListRequest): Page<BasePayloads.SimpleResponse> {
        return wineryRepository.list(request)
    }

    @Transactional(readOnly = true)
    fun detail(uuid: String): WineryPayloads.DetailResponse {
        val winery = wineryGateway.findByUuid(uuid)

        val region = winery.region!!
        val regionResponse =
            BasePayloads.SimpleResponse(region.uuid, region.nameEnglish, region.nameKorean)

        val wines = wineGateway.findByWineryUuid(uuid)
        log.info("wines: \n{}", stringUtils.toPrettyJson(wines))

        return WineryPayloads.DetailResponse(
            winery.uuid,
            winery.nameEnglish,
            winery.nameKorean,
            regionResponse,
            wines
        )
    }

    @Transactional
    fun create(request: WineryPayloads.CreateRequest) {
        val winery: Winery = mapper.fromCreateRequestDto(request)
        wineryRepository.save(winery)
    }

    @Transactional
    fun update(request: WineryPayloads.UpdateRequest) {
        val winery: Winery = wineryGateway.findByUuid(request.uuid)

        val updatedWinery = mapper.fromUpdateRequestDto(request, winery)
        wineryRepository.save(updatedWinery)
    }

    @Transactional
    fun delete(uuid: String) = wineryRepository.delete(wineryGateway.findByUuid(uuid))

}
