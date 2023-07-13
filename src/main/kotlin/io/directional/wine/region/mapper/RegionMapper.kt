package io.directional.wine.region.mapper

import io.directional.wine.common.config.BaseMapperConfig
import io.directional.wine.common.config.BasePayloads
import io.directional.wine.domain.region.Region
import io.directional.wine.dto.region.RegionCSVDto
import io.directional.wine.payload.region.RegionPayloads
import org.mapstruct.*

@Mapper(config = BaseMapperConfig::class)
@DecoratedWith(RegionDecorator::class)
interface RegionMapper {

    @Named("fromRegionCSVDto")
    @Mapping(target = "nameKorean", source = "name_korean")
    @Mapping(target = "nameEnglish", source = "name_english")
    fun fromRegionCSVDto(dto: RegionCSVDto): Region

    @IterableMapping(qualifiedByName = ["fromRegionCSVDto"])
    fun fromRegionCSVDtoList(dtos: List<RegionCSVDto>): List<Region>

    fun updateFromRegionCSVDto(dto: RegionCSVDto, @MappingTarget region: Region): Region

    @Named("listResponse")
    fun listResponse(region: Region): BasePayloads.SimpleResponse

    @IterableMapping(qualifiedByName = ["listResponse"])
    fun listResponses(regions: List<Region>): List<BasePayloads.SimpleResponse>

    fun fromCreateRequestDto(dto: RegionPayloads.CreateRequest): Region

    fun fromUpdateRequestDto(dto: RegionPayloads.UpdateRequest, @MappingTarget region: Region): Region

}
