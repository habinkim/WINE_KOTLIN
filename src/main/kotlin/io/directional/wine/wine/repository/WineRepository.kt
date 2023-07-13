package io.directional.wine.wine.repository;

import io.directional.wine.domain.wine.Wine
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface WineRepository : JpaRepository<Wine, Long>, WineRepositoryCustom {
    fun findByNameEnglish(nameEnglish: String): Optional<Wine>
    fun findByUuid(uuid: String): Optional<Wine>
}
