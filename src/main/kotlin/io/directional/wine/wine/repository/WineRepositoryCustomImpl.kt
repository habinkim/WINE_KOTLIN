package io.directional.wine.wine.repository

import com.querydsl.core.types.Expression
import com.querydsl.core.types.Order
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.PathBuilder
import com.querydsl.jpa.impl.JPAQueryFactory
import io.directional.wine.common.config.BasePayloads
import io.directional.wine.common.config.buildQBean
import io.directional.wine.common.util.PredicateBuilder
import io.directional.wine.domain.grape.QGrape
import io.directional.wine.domain.region.QRegion.region
import io.directional.wine.domain.wine.QImporter.importer
import io.directional.wine.domain.wine.QWine.wine
import io.directional.wine.domain.winery.QWinery.winery
import io.directional.wine.payload.grape.GrapePayloads
import io.directional.wine.payload.wine.WinePayloads
import io.directional.wine.region.service.RegionGateway
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.stereotype.Repository

@Repository
class WineRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory,
    private val regionGateway: RegionGateway
) : WineRepositoryCustom {

    override fun findByRegionUuid(uuid: String): List<BasePayloads.SimpleResponse> {
        return queryFactory.select(
            buildQBean<BasePayloads.SimpleResponse>(
                wine.uuid,
                wine.nameEnglish,
                wine.nameKorean
            )
        )
            .from(wine)
            .join(wine.region, region)
            .where(region.uuid.eq(uuid))
            .fetch()
    }

    override fun findByImporterUuid(uuid: String): List<BasePayloads.SimpleResponse> {
        return queryFactory.select(
            buildQBean<BasePayloads.SimpleResponse>(
                wine.uuid,
                wine.nameEnglish,
                wine.nameKorean
            )
        )
            .from(wine)
            .join(wine.importer, importer)
            .orderBy(wine.nameKorean.asc())
            .where(importer.uuid.eq(uuid))
            .fetch()
    }

    override fun list(request: WinePayloads.ListRequest): Page<WinePayloads.ListResponse> {
        val pageRequest: PageRequest =
            PageRequest.of(request.pageNo!!, request.pageSize!!, Sort.by(Sort.Order.asc("nameKorean")))

        val predicate = PredicateBuilder.builder()
            .eqString(wine.type, request.type)
            .betweenNumberDynamic(wine.alcohol, request.minAlcohol, request.maxAlcohol)
            .betweenNumberDynamic(wine.price, request.minPrice, request.maxPrice)
            .eqString(wine.style, request.style)
            .eqString(wine.grade, request.grade)
            .containsString(wine.nameEnglish, request.nameEnglish)
            .containsString(wine.nameKorean, request.nameKorean)
            .build()

        val fetch = queryFactory.select(
            buildQBean<WinePayloads.ListResponse>(
                wine.uuid,
                wine.type,
                wine.nameEnglish,
                wine.nameKorean,
                region.uuid.`as`("regionUuid")
            )
        )
            .from(wine)
            .where(predicate)
            .join(wine.region, region)
            .offset(pageRequest.offset)
            .limit(pageRequest.pageSize.toLong())
            .orderBy(getOrderSpecifier(request), wine.nameKorean.asc())
            .fetch()

        fetch.forEach{ r ->
            val root = regionGateway.findRoot(r.regionUuid!!)
            r.rootRegion = BasePayloads.SimpleResponse(root.uuid, root.nameEnglish, root.nameKorean)
        }

        val countQuery = queryFactory.select(wine.id.count())
            .from(wine)
            .where(predicate)
            .join(wine.region, region)

        return PageableExecutionUtils.getPage(fetch, pageRequest) { countQuery.fetchOne()!! }
    }

    private fun getOrderSpecifier(request: WinePayloads.ListRequest): OrderSpecifier<*> {
        val orderSpecifier: OrderSpecifier<*> = when {
            request.sort.equals(null) -> wine.nameKorean.asc()
            else -> OrderSpecifier(
                Order.valueOf(request.direction?.name?: "ASC"),
                PathBuilder(wine.getType(), wine.metadata).get(request.sort) as Expression<out Comparable<*>>
            )
        }
        return orderSpecifier
    }

    override fun findByWineryUuid(uuid: String): List<BasePayloads.SimpleResponse> {
        return queryFactory.select(
            buildQBean<BasePayloads.SimpleResponse>(
                wine.uuid,
                wine.nameEnglish,
                wine.nameKorean
            )
        )
            .from(wine)
            .join(wine.winery, winery)
            .orderBy(wine.nameKorean.asc())
            .where(winery.uuid.eq(uuid))
            .fetch()
    }
}
