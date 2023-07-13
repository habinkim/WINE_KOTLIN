package io.directional.wine.grape.repository

import io.directional.wine.common.config.BasePayloads
import io.directional.wine.payload.grape.GrapePayloads
import org.springframework.data.domain.Page

interface GrapeRepositoryCustom {

    fun list(request: GrapePayloads.ListRequest): Page<GrapePayloads.SimpleResponse>
    fun findByRegionUuid(uuid: String) : List<BasePayloads.SimpleResponse>
    fun findRegions(uuid: String) : List<BasePayloads.SimpleResponse>
    fun findWines(uuid: String) : List<BasePayloads.SimpleResponse>

}
