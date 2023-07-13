package io.directional.wine.wine.mapper

import io.directional.wine.common.config.BasePayloads
import io.directional.wine.domain.grape.Grape
import io.directional.wine.domain.wine.Wine
import io.directional.wine.domain.wine.WineGrape
import io.directional.wine.domain.wine.WineTag
import io.directional.wine.dto.wine.WineAromaCSVDto
import io.directional.wine.dto.wine.WineCSVDto
import io.directional.wine.dto.wine.WineGrapeCSVDto
import io.directional.wine.dto.wine.WinePairingCSVDto
import io.directional.wine.grape.repository.GrapeRepository
import io.directional.wine.payload.wine.ImporterPayloads
import io.directional.wine.payload.wine.WinePayloads
import io.directional.wine.region.repository.RegionRepository
import io.directional.wine.region.service.RegionGateway
import io.directional.wine.wine.repository.ImporterRepository
import io.directional.wine.wine.repository.WineGrapeRepository
import io.directional.wine.wine.repository.WineRepository
import io.directional.wine.wine.repository.WineTagRepository
import io.directional.wine.winery.repository.WineryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Primary
import org.springframework.util.StringUtils

@Primary
abstract class WineDecorator : WineMapper {

    @Qualifier("delegate")
    @Autowired
    private lateinit var delegate: WineMapper

    @Autowired
    private lateinit var wineRepository: WineRepository

    @Autowired
    private lateinit var wineTagRepository: WineTagRepository

    @Autowired
    private lateinit var wineGrapeRepository: WineGrapeRepository

    @Autowired
    private lateinit var importerRepository: ImporterRepository

    @Autowired
    private lateinit var grapeRepository: GrapeRepository

    @Autowired
    private lateinit var wineryRepository: WineryRepository

    @Autowired
    private lateinit var regionRepository: RegionRepository

    override fun fromWineCSVDto(dto: WineCSVDto): Wine {
        val wine: Wine = delegate.fromWineCSVDto(dto)

        if (StringUtils.hasText(dto.winery_name_english)) {
            wine.winery = wineryRepository.findByNameEnglish(dto.winery_name_english!!).orElseGet { null }
        }

        if (StringUtils.hasText(dto.region_name_english)) {
            wine.region = regionRepository.findByNameEnglish(dto.region_name_english!!).orElseGet { null }
        }

        if (StringUtils.hasText(dto.importer)) {
            wine.importer = importerRepository.findByName(dto.importer!!).orElseGet { null }
        }

        if (StringUtils.hasText(dto.style)) {
            wine.style = dto.style
        }

        if (StringUtils.hasText(dto.grade)) {
            wine.grade = dto.grade
        }

        return wine
    }

    override fun fromWineCSVDtoList(dtos: List<WineCSVDto>): List<Wine> {
        val list: MutableList<Wine> = ArrayList(dtos.size)
        for (wineCSVDto in dtos) {
            list.add(fromWineCSVDto(wineCSVDto))
        }
        return list
    }

    override fun fromWineGrapeCSVDto(dto: WineGrapeCSVDto): WineGrape {
        val wine: Wine = wineRepository.findByNameEnglish(dto.name_english!!).orElseGet { null }
        val grape: Grape = grapeRepository.findByNameEnglish(dto.grape_name_english).orElseGet { null }

        return WineGrape(wine, grape)
    }

    override fun fromWineGrapeCSVDtoList(dtos: List<WineGrapeCSVDto>): List<WineGrape> {
        val list: MutableList<WineGrape> = ArrayList(dtos.size)
        for (wineGrapeCSVDto in dtos) {
            list.add(fromWineGrapeCSVDto(wineGrapeCSVDto))
        }
        return list
    }

    override fun fromWineAromaCSVDto(dto: WineAromaCSVDto): WineTag {
        val wineTag: WineTag = delegate.fromWineAromaCSVDto(dto)
        wineTag.wine = wineRepository.findByNameEnglish(dto.name_english!!).orElseGet { null }
        return wineTag
    }

    override fun fromWineAromaCSVDtoList(dtos: List<WineAromaCSVDto>): List<WineTag> {
        val list: MutableList<WineTag> = ArrayList(dtos.size)
        for (wineAromaCSVDto in dtos) {
            list.add(fromWineAromaCSVDto(wineAromaCSVDto))
        }
        return list
    }

    override fun fromWinePairingCSVDto(dto: WinePairingCSVDto): WineTag {
        val wineTag: WineTag = delegate.fromWinePairingCSVDto(dto)
        wineTag.wine = wineRepository.findByNameEnglish(dto.name_english!!).orElseGet { null }
        return wineTag
    }

    override fun fromWinePairingCSVDtoList(dtos: List<WinePairingCSVDto>): List<WineTag> {
        val list: MutableList<WineTag> = ArrayList(dtos.size)
        for (winePairingCSVDto in dtos) {
            list.add(fromWinePairingCSVDto(winePairingCSVDto))
        }
        return list
    }

    override fun fromCreateRequest(request: WinePayloads.CreateRequest): Wine {
        val wine = delegate.fromCreateRequest(request)

        if (StringUtils.hasText(request.importerUuid)) {
            wine.importer = importerRepository.findByUuid(request.importerUuid!!).orElseGet { null }
        }

        if (StringUtils.hasText(request.wineryUuid)) {
            wine.winery = wineryRepository.findByUuid(request.wineryUuid!!).orElseGet { null }
        }

        if (StringUtils.hasText(request.regionUuid)) {
            wine.region = regionRepository.findByUuid(request.regionUuid!!).orElseGet { null }
        }

        return wine
    }

    override fun fromUpdateRequest(request: WinePayloads.UpdateRequest, wine: Wine): Wine {
        val updatedWine = delegate.fromUpdateRequest(request, wine)

        if (StringUtils.hasText(request.importerUuid)) {
            updatedWine.importer = importerRepository.findByUuid(request.importerUuid!!).orElseGet { null }
        }

        if (StringUtils.hasText(request.wineryUuid)) {
            updatedWine.winery = wineryRepository.findByUuid(request.wineryUuid!!).orElseGet { null }
        }

        if (StringUtils.hasText(request.regionUuid)) {
            updatedWine.region = regionRepository.findByUuid(request.regionUuid!!).orElseGet { null }
        }

        return updatedWine
    }

    override fun detailResponse(wine: Wine): WinePayloads.DetailResponse {
        val detailResponse = delegate.detailResponse(wine)

        val regionUuid = wine.region!!.uuid!!

        val regions: MutableList<BasePayloads.SimpleResponse> = ArrayList()

        regions.add(
            BasePayloads.SimpleResponse(
                wine.region!!.uuid,
                wine.region!!.nameEnglish,
                wine.region!!.nameKorean
            )
        )

        val root = regionRepository.findRoot(regionUuid)
        detailResponse.rootRegion = BasePayloads.SimpleResponse(root.uuid, root.nameEnglish, root.nameKorean)

        val parents = regionRepository.findParents(regionUuid)
            .map { r -> BasePayloads.SimpleResponse(r.uuid, r.nameEnglish, r.nameKorean) }
        regions.addAll(parents)

        detailResponse.regions = regions

        val wineryRegion = wine.winery?.region
        detailResponse.wineryRegion = BasePayloads.SimpleResponse(
            wineryRegion?.uuid, wineryRegion?.nameEnglish,
            wineryRegion?.nameKorean
        )

        detailResponse.tags = wineTagRepository.findByWineUuid(wine.uuid!!)

        detailResponse.grapes = wineGrapeRepository.findByWineUuid(wine.uuid)

        return detailResponse
    }
}
