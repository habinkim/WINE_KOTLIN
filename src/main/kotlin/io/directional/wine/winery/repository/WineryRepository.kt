package io.directional.wine.winery.repository;

import io.directional.wine.domain.winery.Winery
import io.directional.wine.payload.winery.WineryPayloads
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface WineryRepository : JpaRepository<Winery, Long>, WineryRepositoryCustom {
    fun findByNameEnglish(nameEnglish: String): Optional<Winery>
    fun findByUuid(uuid: String) : Optional<Winery>
}
