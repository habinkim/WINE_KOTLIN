package io.directional.wine.grape.service

import io.directional.wine.common.config.BasePayloads
import io.directional.wine.common.config.logger
import io.directional.wine.common.util.StringUtils
import io.directional.wine.domain.grape.Grape
import io.directional.wine.domain.grape.GrapeShare
import io.directional.wine.grape.mapper.GrapeMapper
import io.directional.wine.grape.repository.GrapeRepository
import io.directional.wine.grape.repository.GrapeShareRepository
import io.directional.wine.payload.grape.GrapePayloads
import io.directional.wine.region.service.RegionGateway
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GrapeService(
    private val grapeRepository: GrapeRepository,
    private val grapeShareRepository: GrapeShareRepository,

    private val grapeGateway: GrapeGateway,
    private val regionGateway: RegionGateway,

    private val mapper: GrapeMapper,

    private val stringUtils: StringUtils
) {

    private val log = logger()

    @Transactional(readOnly = true)
    fun list(request: GrapePayloads.ListRequest): Page<GrapePayloads.SimpleResponse> {
        return grapeRepository.list(request)
    }

    @Transactional(readOnly = true)
    fun detail(uuid: String): GrapePayloads.DetailResponse {
        val grape: Grape = grapeGateway.findByUuid(uuid)

        val regions: List<BasePayloads.SimpleResponse> = grapeGateway.findRegions(uuid)
        log.info("regions: \n{}", stringUtils.toPrettyJson(regions))

        val wines: List<BasePayloads.SimpleResponse> = grapeGateway.findWines(uuid)
        log.info("wines: \n{}", stringUtils.toPrettyJson(wines))

        return GrapePayloads.DetailResponse(
            grape.uuid, grape.nameEnglish, grape.nameKorean, grape.acidity, grape.body, grape.sweetness, grape.tannin,
            regions, wines
        )
    }

    @Transactional
    fun create(request: GrapePayloads.CreateRequest) {
        val grape: Grape = mapper.fromCreateRequestDto(request)
        grapeRepository.save(grape)
    }

    @Transactional
    fun update(request: GrapePayloads.UpdateRequest) {
        val grape: Grape = grapeGateway.findByUuid(request.uuid)

        val updatedGrape = mapper.fromUpdateRequestDto(request, grape)
        grapeRepository.save(updatedGrape)
    }

    @Transactional
    fun delete(uuid: String) = grapeRepository.delete(grapeGateway.findByUuid(uuid))

    @Transactional
    fun createGrapeShare(request: GrapePayloads.CreateShareRequest) {
        val grape = grapeGateway.findByUuid(request.grapeUuid)
        val region = regionGateway.findByUuid(request.regionUuid)

        val grapeShare = GrapeShare(request.share, grape, region)
        grapeShareRepository.save(grapeShare)
    }

}
