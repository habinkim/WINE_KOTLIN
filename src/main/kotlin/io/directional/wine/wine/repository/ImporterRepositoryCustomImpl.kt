package io.directional.wine.wine.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import io.directional.wine.common.config.buildQBean
import io.directional.wine.common.util.PredicateBuilder
import io.directional.wine.domain.wine.QImporter
import io.directional.wine.domain.wine.QImporter.importer
import io.directional.wine.payload.wine.ImporterPayloads
import io.directional.wine.payload.wine.WinePayloads
import org.springframework.stereotype.Repository

@Repository
class ImporterRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory
) : ImporterRepositoryCustom {
    override fun list(name: String?): List<ImporterPayloads.ListResponse> {

        val predicate = PredicateBuilder.builder()
            .containsString(importer.name, name)
            .build()

        return queryFactory.select(
            buildQBean<ImporterPayloads.ListResponse>(
                importer.uuid,
                importer.name
            )
        )
            .from(importer)
            .where(predicate)
            .orderBy(importer.name.asc())
            .fetch()
    }
}
