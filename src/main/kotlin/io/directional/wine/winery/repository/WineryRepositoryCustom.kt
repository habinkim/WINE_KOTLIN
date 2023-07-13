package io.directional.wine.winery.repository

import io.directional.wine.common.config.BasePayloads
import io.directional.wine.payload.winery.WineryPayloads
import org.springframework.data.domain.Page

interface WineryRepositoryCustom {
    fun list(request: WineryPayloads.ListRequest): Page<BasePayloads.SimpleResponse>
    fun findByRegionUuid(uuid: String): List<BasePayloads.SimpleResponse>
}
