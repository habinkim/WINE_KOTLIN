package io.directional.wine.winery.service

import io.directional.wine.common.config.BasePayloads
import io.directional.wine.common.response.MessageCode
import io.directional.wine.domain.winery.Winery
import io.directional.wine.exception.CommonApplicationException
import io.directional.wine.winery.repository.WineryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation.MANDATORY
import org.springframework.transaction.annotation.Transactional

@Service
class WineryGateway(
    private val repository: WineryRepository
) {

    @Transactional(readOnly = true, propagation = MANDATORY)
    fun findByRegionUuid(uuid: String): List<BasePayloads.SimpleResponse> = repository.findByRegionUuid(uuid)

    @Transactional(readOnly = true, propagation = MANDATORY)
    fun findById(id: Long): Winery =
        repository.findById(id).orElseThrow { CommonApplicationException(MessageCode.NOT_FOUND_WINERY) }

    @Transactional(readOnly = true, propagation = MANDATORY)
    fun findByUuid(uuid: String): Winery =
        repository.findByUuid(uuid).orElseThrow { CommonApplicationException(MessageCode.NOT_FOUND_WINERY) }

}
