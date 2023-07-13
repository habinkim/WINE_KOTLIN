package io.directional.wine.common.config

import com.opencsv.bean.CsvToBean
import io.directional.wine.domain.region.Region
import io.directional.wine.domain.wine.Importer
import io.directional.wine.domain.wine.Wine
import io.directional.wine.domain.wine.WineGrape
import io.directional.wine.domain.wine.WineTag
import io.directional.wine.domain.winery.Winery
import io.directional.wine.dto.grape.GrapeCSVDto
import io.directional.wine.dto.grape.GrapeShareCSVDto
import io.directional.wine.dto.region.RegionCSVDto
import io.directional.wine.dto.wine.WineAromaCSVDto
import io.directional.wine.dto.wine.WineCSVDto
import io.directional.wine.dto.wine.WineGrapeCSVDto
import io.directional.wine.dto.wine.WinePairingCSVDto
import io.directional.wine.dto.winery.WineryCSVDto
import io.directional.wine.grape.mapper.GrapeMapper
import io.directional.wine.grape.repository.GrapeRepository
import io.directional.wine.grape.repository.GrapeShareRepository
import io.directional.wine.region.mapper.RegionMapper
import io.directional.wine.region.repository.RegionRepository
import io.directional.wine.wine.mapper.WineMapper
import io.directional.wine.wine.repository.ImporterRepository
import io.directional.wine.wine.repository.WineGrapeRepository
import io.directional.wine.wine.repository.WineRepository
import io.directional.wine.wine.repository.WineTagRepository
import io.directional.wine.winery.mapper.WineryMapper
import io.directional.wine.winery.repository.WineryRepository
import jakarta.annotation.PostConstruct
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation.MANDATORY
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.StringUtils.hasText

private const val REGION_DATA_PATH = "data/region.csv"
private const val GRAPE_DATA_PATH = "data/grape.csv"
private const val GRAPE_SHARE_DATA_PATH = "data/grape_share.csv"
private const val WINERY_DATA_PATH = "data/winery.csv"
private const val WINE_DATA_PATH = "data/wine.csv"
private const val WINE_GRAPE_DATA_PATH = "data/wine_grape.csv"
private const val WINE_AROMA_DATA_PATH = "data/wine_aroma.csv"
private const val WINE_PAIRING_DATA_PATH = "data/wine_pairing.csv"

@ConditionalOnProperty(value = ["data-init.enabled"], havingValue = "true")
@Component
class DataInit(
    private val regionMapper: RegionMapper,
    private val grapeMapper: GrapeMapper,
    private val wineryMapper: WineryMapper,
    private val wineMapper: WineMapper,

    private val regionRepository: RegionRepository,
    private val grapeRepository: GrapeRepository,
    private val grapeShareRepository: GrapeShareRepository,
    private val wineryRepository: WineryRepository,
    private val wineRepository: WineRepository,
    private val importerRepository: ImporterRepository,
    private val wineGrapeRepository: WineGrapeRepository,
    private val wineTagRepository: WineTagRepository
) {

    @PostConstruct
    @Transactional
    fun dataInit() {
        parseRegionData()
        parseGrapeData()
        parseGrapeShareData()
        parseWineryData()
        parseImporterData()
        parseWineData()
        parseWineGrapeData()
        parseWineTagData()
    }

    @Transactional(propagation = MANDATORY)
    fun parseRegionData() {
        val csvReader: CsvToBean<RegionCSVDto> = buildCsvToBean<RegionCSVDto>(REGION_DATA_PATH)
        val dtos: List<RegionCSVDto> = csvReader.parse()

        val regionList = regionMapper.fromRegionCSVDtoList(dtos)
        regionRepository.saveAll(regionList)
        regionRepository.flush()

        val updatedRegionList: List<Region> = dtos.filter { d -> hasText(d.parent_name_english) }
            .map { d ->
                regionMapper.updateFromRegionCSVDto(
                    d,
                    regionRepository.findByNameEnglish(d.name_english!!).orElseGet { null }
                )
            }

        regionRepository.saveAll(updatedRegionList)
    }

    @Transactional(propagation = MANDATORY)
    fun parseGrapeData() {
        val csvReader: CsvToBean<GrapeCSVDto> = buildCsvToBean<GrapeCSVDto>(GRAPE_DATA_PATH)
        val dtos: List<GrapeCSVDto> = csvReader.parse()
        val grapeList = grapeMapper.fromGrapeCSVDtoList(dtos)

        grapeRepository.saveAll(grapeList)

    }

    @Transactional(propagation = MANDATORY)
    fun parseGrapeShareData() {
        val csvReader: CsvToBean<GrapeShareCSVDto> = buildCsvToBean<GrapeShareCSVDto>(GRAPE_SHARE_DATA_PATH)
        val dtos: List<GrapeShareCSVDto> = csvReader.parse()
        val grapeList = grapeMapper.fromGrapeShareCSVDtoList(dtos)

        grapeShareRepository.saveAll(grapeList)
    }

    @Transactional(propagation = MANDATORY)
    fun parseWineryData() {
        val csvReader: CsvToBean<WineryCSVDto> = buildCsvToBean<WineryCSVDto>(WINERY_DATA_PATH)
        val dtos: List<WineryCSVDto> = csvReader.parse()

        val wineryList: List<Winery> = wineryMapper.fromWineryCSVDtoList(dtos)
        wineryRepository.saveAll(wineryList)
    }

    @Transactional(propagation = MANDATORY)
    fun parseImporterData() {
        val csvReader: CsvToBean<WineCSVDto> = buildCsvToBean<WineCSVDto>(WINE_DATA_PATH)
        val list: List<String?> = csvReader.parse().stream().map { w -> w.importer }.distinct().toList()

        val importerList: List<Importer> = wineMapper.buildImporterList(list)
        importerRepository.saveAll(importerList)
    }

    @Transactional(propagation = MANDATORY)
    fun parseWineData() {
        val csvReader: CsvToBean<WineCSVDto> = buildCsvToBean<WineCSVDto>(WINE_DATA_PATH)
        val dtos: List<WineCSVDto> = csvReader.parse()

        val wineList: List<Wine> = wineMapper.fromWineCSVDtoList(dtos)
        wineRepository.saveAll(wineList)
    }

    @Transactional(propagation = MANDATORY)
    fun parseWineGrapeData() {
        val csvReader: CsvToBean<WineGrapeCSVDto> = buildCsvToBean<WineGrapeCSVDto>(WINE_GRAPE_DATA_PATH)
        val dtos: List<WineGrapeCSVDto> = csvReader.parse()

        val wineGrapeList: List<WineGrape> = wineMapper.fromWineGrapeCSVDtoList(dtos)
        wineGrapeRepository.saveAll(wineGrapeList)
    }

    @Transactional(propagation = MANDATORY)
    fun parseWineTagData() {
        val wineAromaCsvReader: CsvToBean<WineAromaCSVDto> = buildCsvToBean<WineAromaCSVDto>(WINE_AROMA_DATA_PATH)
        val wineAromaCSVDtos: List<WineAromaCSVDto> = wineAromaCsvReader.parse()

        val wineAromaList: List<WineTag> = wineMapper.fromWineAromaCSVDtoList(wineAromaCSVDtos)
        wineTagRepository.saveAll(wineAromaList)


        val winePairingCsvReader: CsvToBean<WinePairingCSVDto> =
            buildCsvToBean<WinePairingCSVDto>(WINE_PAIRING_DATA_PATH)
        val winePairingCSVDtos: List<WinePairingCSVDto> = winePairingCsvReader.parse()

        val winePairingList: List<WineTag> = wineMapper.fromWinePairingCSVDtoList(winePairingCSVDtos)
        wineTagRepository.saveAll(winePairingList)
    }

}
