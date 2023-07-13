package io.directional.wine.region.repository

import com.querydsl.core.types.Predicate
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import io.directional.wine.common.config.BasePayloads
import io.directional.wine.common.config.buildQBean
import io.directional.wine.common.util.PredicateBuilder
import io.directional.wine.domain.region.QRegion
import io.directional.wine.domain.region.QRegion.region
import io.directional.wine.domain.region.Region
import io.directional.wine.payload.region.RegionPayloads
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Order.asc
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.stereotype.Repository

@Repository
class RegionRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory
) : RegionRepositoryCustom {

    override fun list(request: RegionPayloads.ListRequest): Page<BasePayloads.SimpleResponse> {
        val pageRequest: PageRequest = PageRequest.of(request.pageNo!!, request.pageSize!!, Sort.by(asc("nameKorean")))

        val parentRegion = QRegion("parent")

        val predicate: Predicate? = PredicateBuilder.builder()
            .eqString(parentRegion.uuid, request.parentUuid)
            .containsString(region.nameEnglish, request.nameEnglish)
            .containsString(region.nameKorean, request.nameKorean)
            .build()


        val fetch: List<BasePayloads.SimpleResponse> = queryFactory.select(
            buildQBean<BasePayloads.SimpleResponse>(
                region.uuid,
                region.nameEnglish,
                region.nameKorean
            )
        )
            .from(region)
            .leftJoin(region.parent, parentRegion)
            .where(predicate)
            .orderBy(region.nameKorean.asc())
            .limit(pageRequest.pageSize.toLong())
            .offset(pageRequest.offset)
            .fetch()

        val countQuery: JPAQuery<Long> = queryFactory
            .select(region.count()).from(region)
            .leftJoin(region.parent, parentRegion)
            .where(predicate)

        return PageableExecutionUtils.getPage(fetch, pageRequest) { countQuery.fetchOne()!! }
    }

    override fun findParents(uuid: String): List<Region> {
        val parentRegion = QRegion("parent")
        var parents: MutableList<Region> = ArrayList()

        var currentParent: Region? = findParentByUuid(parentRegion, uuid)
        parents.add(currentParent!!)

        while (currentParent?.parent != null) {
            val findParentByUuid = findParentByUuid(parentRegion, currentParent.uuid!!)
            parents.add(findParentByUuid!!)
            currentParent = findParentByUuid
        }

        return parents
    }

    override fun findRoot(uuid: String): Region {
        val parentRegion = QRegion("parent")

        var currentParent: Region? = findParentByUuid(parentRegion, uuid)

        while (currentParent?.parent != null) {
            val findParentByUuid = findParentByUuid(parentRegion, currentParent.uuid!!)
            currentParent = findParentByUuid
        }

        return currentParent!!
    }

    private fun findParentByUuid(
        parentRegion: QRegion,
        uuid: String
    ): Region? {
        return queryFactory.select(parentRegion)
            .from(region)
            .join(region.parent, parentRegion)
            .where(region.uuid.eq(uuid))
            .fetchOne()
    }


}
