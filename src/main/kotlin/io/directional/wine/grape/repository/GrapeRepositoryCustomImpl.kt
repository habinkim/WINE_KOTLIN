package io.directional.wine.grape.repository

import com.querydsl.core.types.Expression
import com.querydsl.core.types.Order
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.Predicate
import com.querydsl.core.types.dsl.PathBuilder
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.impl.JPAQueryFactory
import io.directional.wine.common.config.BasePayloads
import io.directional.wine.common.config.buildQBean
import io.directional.wine.common.util.PredicateBuilder
import io.directional.wine.domain.grape.QGrape.grape
import io.directional.wine.domain.grape.QGrapeShare.grapeShare
import io.directional.wine.domain.region.QRegion.region
import io.directional.wine.domain.wine.QWine.wine
import io.directional.wine.domain.wine.QWineGrape.wineGrape
import io.directional.wine.payload.grape.GrapePayloads
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.stereotype.Repository

@Repository
class GrapeRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory
) : GrapeRepositoryCustom {

    override fun list(request: GrapePayloads.ListRequest): Page<GrapePayloads.SimpleResponse> {
        val pageRequest: PageRequest =
            PageRequest.of(request.pageNo!!, request.pageSize!!, Sort.by(Sort.Order.asc("nameKorean")))

        val predicate = getPredicate(request)
        val subQuery: Predicate? = getSubQuery(request)
        val orderSpecifier: OrderSpecifier<*> = getOrderSpecifier(request)

        val fetch = queryFactory
            .selectFrom(grape)
            .where(subQuery, predicate)
            .orderBy(orderSpecifier, grape.nameEnglish.asc())
            .limit(pageRequest.pageSize.toLong())
            .offset(pageRequest.offset)
            .fetch()

        val response = fetch.map { g ->
            GrapePayloads.SimpleResponse(g.uuid, g.nameEnglish, g.nameKorean,
                g.grapeShares.map { gs ->
                    BasePayloads.SimpleResponse(
                        gs.region?.uuid,
                        gs.region?.nameEnglish,
                        gs.region?.nameKorean
                    )
                })
        }

        val countQuery = queryFactory
            .select(grape.id.count())
            .from(grape)
            .where(subQuery)

        return PageableExecutionUtils.getPage(response, pageRequest) { countQuery.fetchOne()!! }
    }

    private fun getPredicate(request: GrapePayloads.ListRequest) =
        PredicateBuilder.builder()
            .containsString(grape.nameEnglish, request.nameEnglish)
            .containsString(grape.nameKorean, request.nameKorean)
            .build()

    private fun getSubQuery(request: GrapePayloads.ListRequest): Predicate? {
        val subQuery: Predicate? = when {
            request.regionUuid != null -> grape.id.`in`(
                JPAExpressions.select(grape.id)
                    .from(grapeShare)
                    .join(grapeShare.grape, grape)
                    .join(grapeShare.region, region)
                    .where(region.uuid.eq(request.regionUuid))
            )

            else -> null
        }
        return subQuery
    }

    private fun getOrderSpecifier(request: GrapePayloads.ListRequest): OrderSpecifier<*> {
        val orderSpecifier: OrderSpecifier<*> = when {
            request.sort.equals(null) -> grape.nameKorean.asc()
            else -> OrderSpecifier(
                Order.valueOf(request.direction!!.name),
                PathBuilder(grape.type, grape.metadata).get(request.sort) as Expression<out Comparable<*>>
            )
        }
        return orderSpecifier
    }

    override fun findByRegionUuid(uuid: String): List<BasePayloads.SimpleResponse> {
        return queryFactory.select(
            buildQBean<BasePayloads.SimpleResponse>(
                grape.uuid,
                grape.nameEnglish,
                grape.nameKorean
            )
        )
            .from(grapeShare)
            .join(grapeShare.grape, grape)
            .join(grapeShare.region, region)
            .where(region.uuid.eq(uuid))
            .fetch()
    }

    override fun findRegions(uuid: String): List<BasePayloads.SimpleResponse> {
        return queryFactory.select(
            buildQBean<BasePayloads.SimpleResponse>(
                region.uuid,
                region.nameEnglish,
                region.nameKorean
            )
        )
            .from(grapeShare)
            .join(grapeShare.grape, grape)
            .join(grapeShare.region, region)
            .orderBy(grape.nameKorean.asc())
            .where(grape.uuid.eq(uuid))
            .fetch()
    }

    override fun findWines(uuid: String): List<BasePayloads.SimpleResponse> {
        return queryFactory.select(
            buildQBean<BasePayloads.SimpleResponse>(
                wine.uuid,
                wine.nameEnglish,
                wine.nameKorean
            )
        )
            .from(wineGrape)
            .join(wineGrape.grape, grape)
            .join(wineGrape.wine, wine)
            .orderBy(grape.nameKorean.asc())
            .where(grape.uuid.eq(uuid))
            .fetch()
    }

}
