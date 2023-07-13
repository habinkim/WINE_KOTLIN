package io.directional.wine.region.repository

import io.directional.wine.common.config.BasePayloads
import io.directional.wine.domain.region.Region
import io.directional.wine.payload.region.RegionPayloads
import org.springframework.data.domain.Page

interface RegionRepositoryCustom {
    fun list(request: RegionPayloads.ListRequest): Page<BasePayloads.SimpleResponse>
    fun findParents(uuid: String): List<Region>
    fun findRoot(uuid: String): Region
}
