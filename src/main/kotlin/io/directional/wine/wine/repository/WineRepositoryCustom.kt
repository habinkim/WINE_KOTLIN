package io.directional.wine.wine.repository

import io.directional.wine.common.config.BasePayloads
import io.directional.wine.payload.wine.WinePayloads
import org.springframework.data.domain.Page

interface WineRepositoryCustom {

    fun findByRegionUuid(uuid: String): List<BasePayloads.SimpleResponse>
    fun findByImporterUuid(uuid: String): List<BasePayloads.SimpleResponse>
    fun findByWineryUuid(uuid: String): List<BasePayloads.SimpleResponse>
    fun list(request: WinePayloads.ListRequest) : Page<WinePayloads.ListResponse>

}
