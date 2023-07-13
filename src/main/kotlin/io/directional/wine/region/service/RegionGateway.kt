package io.directional.wine.region.service

import io.directional.wine.common.response.MessageCode
import io.directional.wine.domain.region.Region
import io.directional.wine.exception.CommonApplicationException
import io.directional.wine.region.repository.RegionRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
class RegionGateway(
    private val repository: RegionRepository
) {

    @Transactional(readOnly = true)
    fun findByUuid(uuid: String): Region =
        repository.findByUuid(uuid).orElseThrow { CommonApplicationException(MessageCode.NOT_FOUND_REGION) }

    @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
    fun findById(id: Long): Region =
        repository.findById(id).orElseThrow { CommonApplicationException(MessageCode.NOT_FOUND_REGION) }

    @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
    fun findParents(uuid: String): List<Region> = repository.findParents(uuid)

    @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
    fun findRoot(uuid: String): Region = repository.findRoot(uuid)
}
