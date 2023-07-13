package io.directional.wine.wine.service

import io.directional.wine.common.config.BasePayloads
import io.directional.wine.common.config.logger
import io.directional.wine.common.util.StringUtils
import io.directional.wine.domain.wine.Importer
import io.directional.wine.payload.wine.ImporterPayloads
import io.directional.wine.payload.wine.WinePayloads
import io.directional.wine.wine.mapper.WineMapper
import io.directional.wine.wine.repository.ImporterRepository
import io.directional.wine.wine.repository.WineRepository
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class WineService(
    private val wineRepository: WineRepository,
    private val importerRepository: ImporterRepository,

    private val wineMapper: WineMapper,

    private val wineGateway: WineGateway,

    private val stringUtils: StringUtils
) {

    private val log = logger()

    @Transactional(readOnly = true)
    fun importerList(name: String?): List<ImporterPayloads.ListResponse> {
        return importerRepository.list(name)
    }

    @Transactional(readOnly = true)
    fun importerDetail(uuid: String): ImporterPayloads.DetailResponse {
        val importer: Importer = wineGateway.findImporterByUuid(uuid)

        val wines: List<BasePayloads.SimpleResponse> = wineGateway.findByImporterUuid(uuid)
        log.info("wines : \n{}", stringUtils.toPrettyJson(wines))

        return ImporterPayloads.DetailResponse(importer.uuid, importer.name, wines)
    }

    @Transactional
    fun createImporter(request: ImporterPayloads.CreateRequest) {
        val importer = Importer(request.name)
        importerRepository.save(importer)
    }

    @Transactional
    fun updateImporter(request: ImporterPayloads.UpdateRequest) {
        val importer: Importer = wineGateway.findImporterByUuid(request.uuid)
        importer.name = request.name
        importerRepository.save(importer)
    }

    @Transactional
    fun deleteImporter(uuid: String) =
        importerRepository.delete(wineGateway.findImporterByUuid(uuid))

    @Transactional
    fun wineList(request: WinePayloads.ListRequest): Page<WinePayloads.ListResponse> {
        return wineRepository.list(request)
    }

    //    필터링: 와인 종류, 알코올 도수 범위, 와인의 가격 범위, 와인의 스타일, 와인의 등급, 지역
//    정렬: 와인 이름, 알코올 도수, 산도, 바디감, 단맛, 타닌, 와인의 점수, 와인의 가격
//    검색: 와인 이름
//    다수 조회 시: 와인의 종류, 와인 이름, 최상위 지역 이름
//    단일 조회 시: 와인의 종류, 와인 이름, 알코올 도수, 산도, 바디감, 단맛, 타닌, 와인의 점수, 와인의 가격, 와인의 스타일, 와인의 등급, 수입사 이름, 와이너리 이름, 와이너리
//    지역, 지역 이름 및 모든 상위 지역 이름, 와인의 향, 와인과 어울리는 음식, 포도 품종
    @Transactional
    fun wineDetail(uuid: String): WinePayloads.DetailResponse {
        val wine = wineGateway.findByUuid(uuid)
        return wineMapper.detailResponse(wine)
    }

    @Transactional
    fun createWine(request: WinePayloads.CreateRequest) {
        val wine = wineMapper.fromCreateRequest(request)
        wineRepository.save(wine)
    }

    @Transactional
    fun updateWine(request: WinePayloads.UpdateRequest) {
        val wine = wineGateway.findByUuid(request.uuid)

        val updatedWine = wineMapper.fromUpdateRequest(request, wine)
        wineRepository.save(updatedWine)
    }

    @Transactional
    fun deleteWine(uuid: String) =
        wineRepository.delete(wineGateway.findByUuid(uuid))

}
