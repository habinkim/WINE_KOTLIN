package io.directional.wine.wine.repository;

import io.directional.wine.domain.wine.WineTag
import org.springframework.data.jpa.repository.JpaRepository

interface WineTagRepository : JpaRepository<WineTag, Long>, WineTagRepositoryCustom {
}
