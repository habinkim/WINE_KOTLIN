package io.directional.wine.grape.mapper

import io.directional.wine.common.config.BaseMapperConfig
import io.directional.wine.domain.grape.Grape
import io.directional.wine.domain.grape.GrapeShare
import io.directional.wine.domain.region.Region
import io.directional.wine.dto.grape.GrapeCSVDto
import io.directional.wine.dto.grape.GrapeShareCSVDto
import io.directional.wine.payload.grape.GrapePayloads
import io.directional.wine.payload.region.RegionPayloads
import org.mapstruct.*

@Mapper(config = BaseMapperConfig::class)
@DecoratedWith(GrapeDecorator::class)
interface GrapeMapper {

    @Named("fromGrapeCSVDto")
    @Mapping(target = "nameKorean", source = "name_korean")
    @Mapping(target = "nameEnglish", source = "name_english")
    fun fromGrapeCSVDto(dto: GrapeCSVDto): Grape

    @IterableMapping(qualifiedByName = ["fromGrapeCSVDto"])
    fun fromGrapeCSVDtoList(dtos: List<GrapeCSVDto>): List<Grape>

    @Named("fromGrapeShareCSVDto")
    fun fromGrapeShareCSVDto(dto: GrapeShareCSVDto): GrapeShare

    @IterableMapping(qualifiedByName = ["fromGrapeShareCSVDto"])
    fun fromGrapeShareCSVDtoList(dtos: List<GrapeShareCSVDto>): List<GrapeShare>

    fun fromCreateRequestDto(request: GrapePayloads.CreateRequest): Grape

    fun fromUpdateRequestDto(dto: GrapePayloads.UpdateRequest, @MappingTarget region: Grape): Grape

}
