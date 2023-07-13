package io.directional.wine.wine.repository;

import io.directional.wine.domain.wine.Importer
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ImporterRepository : JpaRepository<Importer, Long>, ImporterRepositoryCustom {
    fun findByName(name: String): Optional<Importer>
    fun findByUuid(uuid: String): Optional<Importer>
}
