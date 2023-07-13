package io.directional.wine.grape.service

import io.directional.wine.common.config.BasePayloads
import io.directional.wine.common.response.MessageCode
import io.directional.wine.domain.grape.Grape
import io.directional.wine.domain.grape.GrapeShare
import io.directional.wine.exception.CommonApplicationException
import io.directional.wine.grape.repository.GrapeRepository
import io.directional.wine.grape.repository.GrapeShareRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
class GrapeGateway(
    private val grapeRepository: GrapeRepository,
    private val grapeShareRepository: GrapeShareRepository
) {

    @Transactional(readOnly = true)
    fun findByUuid(uuid: String): Grape =
        grapeRepository.findByUuid(uuid).orElseThrow { CommonApplicationException(MessageCode.NOT_FOUND_GRAPE) }

    @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
    fun findById(id: Long): Grape =
        grapeRepository.findById(id).orElseThrow { CommonApplicationException(MessageCode.NOT_FOUND_GRAPE) }

    @Transactional(readOnly = true)
    fun findByRegionUuid(uuid: String): List<BasePayloads.SimpleResponse> = grapeRepository.findByRegionUuid(uuid)

    @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
    fun findRegions(uuid: String): List<BasePayloads.SimpleResponse> = grapeRepository.findRegions(uuid)

    @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
    fun findWines(uuid: String): List<BasePayloads.SimpleResponse> = grapeRepository.findWines(uuid)

    @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
    fun findGrapeShare(grapeUuid: String, regionUuid: String): GrapeShare =
        grapeShareRepository.findByGrapeUuidAndRegionUuid(grapeUuid, regionUuid)
            .orElseThrow { CommonApplicationException(MessageCode.NOT_FOUND_GRAPE_SHARE) }

}
