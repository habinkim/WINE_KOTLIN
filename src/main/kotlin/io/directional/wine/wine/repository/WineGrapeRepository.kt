package io.directional.wine.wine.repository;

import io.directional.wine.common.config.BasePayloads
import io.directional.wine.domain.wine.WineGrape
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository

interface WineGrapeRepository : JpaRepository<WineGrape, Long>, WineGrapeRepositoryCustom {
}
