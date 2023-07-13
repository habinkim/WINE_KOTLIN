package io.directional.wine.wine.repository

import io.directional.wine.common.enums.WineTagType

interface WineTagRepositoryCustom {

    fun findByWineUuid(wineUuid: String) : Map<WineTagType, List<String>>

}
