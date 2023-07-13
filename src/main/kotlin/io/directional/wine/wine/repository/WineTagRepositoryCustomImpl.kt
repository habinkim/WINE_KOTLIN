package io.directional.wine.wine.repository

import com.querydsl.core.group.GroupBy
import com.querydsl.core.group.GroupBy.groupBy
import com.querydsl.core.group.GroupBy.list
import com.querydsl.jpa.impl.JPAQueryFactory
import io.directional.wine.common.config.buildQBean
import io.directional.wine.common.enums.WineTagType
import io.directional.wine.domain.wine.QWine
import io.directional.wine.domain.wine.QWine.wine
import io.directional.wine.domain.wine.QWineTag
import io.directional.wine.domain.wine.QWineTag.wineTag
import io.directional.wine.payload.wine.WineTagPayloads
import org.springframework.stereotype.Repository

@Repository
class WineTagRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory
) : WineTagRepositoryCustom {
    override fun findByWineUuid(wineUuid: String): Map<WineTagType, List<String>> {
        return queryFactory
            .from(wineTag)
            .join(wineTag.wine, wine)
            .where(wine.uuid.eq(wineUuid))
            .transform(groupBy(wineTag.type).`as`(list(wineTag.value)))
    }


}
