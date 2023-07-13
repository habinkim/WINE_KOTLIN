package io.directional.wine.wine.repository

import io.directional.wine.common.config.BasePayloads

interface WineGrapeRepositoryCustom {

    fun findByWineUuid(wineUuid: String) : List<BasePayloads.SimpleResponse>

}
