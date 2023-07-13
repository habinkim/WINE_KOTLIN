package io.directional.wine.region.repository

import io.directional.wine.domain.region.Region
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface RegionRepository : JpaRepository<Region, Long>, RegionRepositoryCustom {
    fun findByNameEnglish(nameEnglish: String): Optional<Region>

    fun findByUuid(uuid: String) :Optional<Region>
}
