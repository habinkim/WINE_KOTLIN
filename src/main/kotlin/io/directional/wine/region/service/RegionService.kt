package io.directional.wine.region.service

import io.directional.wine.common.config.BasePayloads
import io.directional.wine.common.config.logger
import io.directional.wine.common.util.StringUtils
import io.directional.wine.domain.region.Region
import io.directional.wine.grape.service.GrapeGateway
import io.directional.wine.payload.region.RegionPayloads
import io.directional.wine.region.mapper.RegionMapper
import io.directional.wine.region.repository.RegionRepository
import io.directional.wine.wine.service.WineGateway
import io.directional.wine.winery.service.WineryGateway
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RegionService(
    private val repository: RegionRepository,

    private val regionGateway: RegionGateway,
    private val grapeGateway: GrapeGateway,
    private val wineryGateway: WineryGateway,
    private val wineGateway: WineGateway,

    private val mapper: RegionMapper,

    private val stringUtils: StringUtils
) {

    private val log = logger()

    @Transactional(readOnly = true)
    fun list(request: RegionPayloads.ListRequest): Page<BasePayloads.SimpleResponse> {
        return repository.list(request)
    }

    @Transactional(readOnly = true)
    fun detail(uuid: String): RegionPayloads.DetailResponse {
        val region: Region = regionGateway.findByUuid(uuid)

        val parent: List<BasePayloads.SimpleResponse> = when {
            region.parent != null -> mapper.listResponses(regionGateway.findParents(uuid))
            else -> ArrayList()
        }
        log.info("parents: \n{}", stringUtils.toPrettyJson(parent))

        val grapes = grapeGateway.findByRegionUuid(uuid)
        log.info("grapes: \n{}", stringUtils.toPrettyJson(grapes))

        val winerys = wineryGateway.findByRegionUuid(uuid)
        log.info("winerys: \n{}", stringUtils.toPrettyJson(winerys))

        val wines = wineGateway.findByRegionUuid(uuid)
        log.info("wines: \n{}", stringUtils.toPrettyJson(wines))

        return RegionPayloads.DetailResponse(
            region.uuid,
            region.nameEnglish,
            region.nameKorean,
            parent,
            grapes,
            winerys,
            wines
        )
    }

    @Transactional
    fun create(request: RegionPayloads.CreateRequest) {
        val region: Region = mapper.fromCreateRequestDto(request)
        repository.save(region)
    }

    @Transactional
    fun update(request: RegionPayloads.UpdateRequest) {
        val region: Region = regionGateway.findByUuid(request.uuid)

        val updatedRegion = mapper.fromUpdateRequestDto(request, region)
        repository.save(updatedRegion)
    }

    @Transactional
    fun delete(uuid: String) = repository.delete(regionGateway.findByUuid(uuid))

}
