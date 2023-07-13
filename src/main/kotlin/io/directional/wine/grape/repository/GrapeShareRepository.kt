package io.directional.wine.grape.repository;

import io.directional.wine.domain.grape.GrapeShare
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface GrapeShareRepository : JpaRepository<GrapeShare, Long> {
    @EntityGraph(attributePaths = ["grape", "region"])
    fun findByGrapeUuidAndRegionUuid(grapeUuid: String, regionUuid: String): Optional<GrapeShare>
}
