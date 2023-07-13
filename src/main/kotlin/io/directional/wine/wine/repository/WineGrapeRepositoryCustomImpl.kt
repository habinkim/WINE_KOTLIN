package io.directional.wine.wine.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import io.directional.wine.common.config.BasePayloads
import io.directional.wine.common.config.buildQBean
import io.directional.wine.domain.grape.QGrape
import io.directional.wine.domain.grape.QGrape.grape
import io.directional.wine.domain.wine.QWine
import io.directional.wine.domain.wine.QWine.wine
import io.directional.wine.domain.wine.QWineGrape
import io.directional.wine.domain.wine.QWineGrape.wineGrape
import org.springframework.stereotype.Repository

@Repository
class WineGrapeRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory
) : WineGrapeRepositoryCustom {

    override fun findByWineUuid(wineUuid: String): List<BasePayloads.SimpleResponse> {
        return queryFactory.select(
            buildQBean<BasePayloads.SimpleResponse>(
                grape.uuid,
                grape.nameEnglish,
                grape.nameKorean
            )
        )
            .from(wineGrape)
            .join(wineGrape.wine, wine)
            .join(wineGrape.grape, grape)
            .where(wine.uuid.eq(wineUuid))
            .fetch()
    }
}
