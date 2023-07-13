package io.directional.wine.winery.mapper

import io.directional.wine.common.config.BaseMapperConfig
import io.directional.wine.domain.winery.Winery
import io.directional.wine.dto.winery.WineryCSVDto
import io.directional.wine.payload.winery.WineryPayloads
import org.mapstruct.*

@Mapper(config = BaseMapperConfig::class)
@DecoratedWith(WineryDecorator::class)
interface WineryMapper {

    @Named("fromWineryCSVDto")
    @Mapping(target = "nameKorean", source = "name_korean")
    @Mapping(target = "nameEnglish", source = "name_english")
    fun fromWineryCSVDto(dto: WineryCSVDto): Winery

    @IterableMapping(qualifiedByName = ["fromWineryCSVDto"])
    fun fromWineryCSVDtoList(dtos: List<WineryCSVDto>): List<Winery>

    fun fromCreateRequestDto(request: WineryPayloads.CreateRequest): Winery
    fun fromUpdateRequestDto(request: WineryPayloads.UpdateRequest, @MappingTarget winery: Winery): Winery

}
