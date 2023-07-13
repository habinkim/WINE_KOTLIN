package io.directional.wine.winery.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import io.directional.wine.common.config.BasePayloads
import io.directional.wine.common.config.buildQBean
import io.directional.wine.common.util.PredicateBuilder
import io.directional.wine.domain.region.QRegion.region
import io.directional.wine.domain.winery.QWinery.winery
import io.directional.wine.payload.winery.WineryPayloads
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.stereotype.Repository

@Repository
class WineryRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory
) : WineryRepositoryCustom {

    override fun list(request: WineryPayloads.ListRequest): Page<BasePayloads.SimpleResponse> {
        val pageRequest: PageRequest =
            PageRequest.of(request.pageNo!!, request.pageSize!!, Sort.by(Sort.Order.asc("nameKorean")))

        val predicate = PredicateBuilder.builder()
            .eqString(region.uuid, request.regionUuid)
            .containsString(winery.nameEnglish, request.nameEnglish)
            .containsString(winery.nameKorean, request.nameKorean)
            .build()

        val fetch = queryFactory.select(
            buildQBean<BasePayloads.SimpleResponse>(
                winery.uuid,
                winery.nameEnglish,
                winery.nameKorean
            )
        )
            .from(winery)
            .join(winery.region, region)
            .where(predicate)
            .offset(pageRequest.offset)
            .limit(pageRequest.pageSize.toLong())
            .orderBy(winery.nameKorean.asc())
            .fetch()

        val countQuery = queryFactory.select(winery.id.count())
            .from(winery)
            .join(winery.region, region)
            .where(predicate)

        return PageableExecutionUtils.getPage(fetch, pageRequest) {countQuery.fetchOne()!!}
    }

    override fun findByRegionUuid(uuid: String): List<BasePayloads.SimpleResponse> {
        return queryFactory.select(
            buildQBean<BasePayloads.SimpleResponse>(
                winery.uuid,
                winery.nameEnglish,
                winery.nameKorean
            )
        )
            .from(winery)
            .join(winery.region, region)
            .where(region.uuid.eq(uuid))
            .fetch()
    }

}
