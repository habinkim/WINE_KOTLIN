package io.directional.wine.wine.mapper

import io.directional.wine.common.config.BaseMapperConfig
import io.directional.wine.domain.wine.Importer
import io.directional.wine.domain.wine.Wine
import io.directional.wine.domain.wine.WineGrape
import io.directional.wine.domain.wine.WineTag
import io.directional.wine.dto.wine.WineAromaCSVDto
import io.directional.wine.dto.wine.WineCSVDto
import io.directional.wine.dto.wine.WineGrapeCSVDto
import io.directional.wine.dto.wine.WinePairingCSVDto
import io.directional.wine.payload.wine.WinePayloads
import org.mapstruct.*

@Mapper(config = BaseMapperConfig::class)
@DecoratedWith(WineDecorator::class)
interface WineMapper {

    @Named("fromWineCSVDto")
    @Mapping(target = "nameKorean", source = "name_korean")
    @Mapping(target = "nameEnglish", source = "name_english")
    @Mapping(target = "servingTemperature", source = "serving_temperature")
    @Mapping(target = "style", ignore = true)
    @Mapping(target = "grade", ignore = true)
    @Mapping(target = "importer", ignore = true)
    fun fromWineCSVDto(dto: WineCSVDto): Wine

    @IterableMapping(qualifiedByName = ["fromWineCSVDto"])
    fun fromWineCSVDtoList(dtos: List<WineCSVDto>): List<Wine>


    fun fromWineGrapeCSVDto(dto: WineGrapeCSVDto): WineGrape

    fun fromWineGrapeCSVDtoList(dtos: List<WineGrapeCSVDto>): List<WineGrape>


    @Named("fromWineAromaCSVDto")
    @Mapping(target = "type", constant = "AROMA")
    @Mapping(target = "value", source = "aroma")
    fun fromWineAromaCSVDto(dto: WineAromaCSVDto): WineTag

    @IterableMapping(qualifiedByName = ["fromWineAromaCSVDto"])
    fun fromWineAromaCSVDtoList(dtos: List<WineAromaCSVDto>): List<WineTag>

    @Named("fromWinePairingCSVDto")
    @Mapping(target = "type", constant = "PAIRING")
    @Mapping(target = "value", source = "pairing")
    fun fromWinePairingCSVDto(dto: WinePairingCSVDto): WineTag

    @IterableMapping(qualifiedByName = ["fromWinePairingCSVDto"])
    fun fromWinePairingCSVDtoList(dtos: List<WinePairingCSVDto>): List<WineTag>

    @Named("buildImporter")
    fun buildImporter(name: String) : Importer

    @IterableMapping(qualifiedByName = ["buildImporter"])
    fun buildImporterList(name: List<String?>) : List<Importer>

    fun fromCreateRequest(request: WinePayloads.CreateRequest) : Wine

    fun fromUpdateRequest(request: WinePayloads.UpdateRequest, @MappingTarget wine: Wine) : Wine

    fun detailResponse(wine: Wine) : WinePayloads.DetailResponse
}
