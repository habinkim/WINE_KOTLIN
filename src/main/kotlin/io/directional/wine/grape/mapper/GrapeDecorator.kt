package io.directional.wine.grape.mapper

import io.directional.wine.common.config.BasePayloads
import io.directional.wine.domain.grape.Grape
import io.directional.wine.domain.grape.GrapeShare
import io.directional.wine.domain.region.Region
import io.directional.wine.dto.grape.GrapeShareCSVDto
import io.directional.wine.grape.repository.GrapeRepository
import io.directional.wine.payload.grape.GrapePayloads
import io.directional.wine.region.repository.RegionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Primary

@Primary
abstract class GrapeDecorator : GrapeMapper {

    @Qualifier("delegate")
    @Autowired
    private lateinit var delegate: GrapeMapper

    @Autowired
    private lateinit var grapeRepository: GrapeRepository

    @Autowired
    private lateinit var regionRepository: RegionRepository

    override fun fromGrapeShareCSVDto(dto: GrapeShareCSVDto): GrapeShare {
        var grapeShare: GrapeShare = delegate.fromGrapeShareCSVDto(dto)

        val grape: Grape = grapeRepository.findByNameEnglish(dto.name_english).orElseGet { null }
        grapeShare.grape = grape

        val region: Region? = regionRepository.findByNameEnglish(dto.region_name_english!!).orElseGet { null }
        grapeShare.region = region

        return grapeShare
    }

    override fun fromGrapeShareCSVDtoList(dtos: List<GrapeShareCSVDto>): List<GrapeShare> {
        val list: MutableList<GrapeShare> = ArrayList(dtos.size)
        for (grapeShareCSVDto in dtos) {
            list.add(fromGrapeShareCSVDto(grapeShareCSVDto))
        }
        return list
    }
}
