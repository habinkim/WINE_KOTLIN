package io.directional.wine.grape.repository;

import io.directional.wine.domain.grape.Grape
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface GrapeRepository : JpaRepository<Grape, Long>, GrapeRepositoryCustom {
    fun findByNameEnglish(nameEnglish : String?) : Optional<Grape>

    fun findByUuid(uuid: String) : Optional<Grape>
}
