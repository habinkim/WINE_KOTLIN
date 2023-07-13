package io.directional.wine.wine.service

import io.directional.wine.common.config.BasePayloads
import io.directional.wine.common.response.MessageCode
import io.directional.wine.domain.wine.Importer
import io.directional.wine.domain.wine.Wine
import io.directional.wine.exception.CommonApplicationException
import io.directional.wine.wine.repository.ImporterRepository
import io.directional.wine.wine.repository.WineRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation.MANDATORY
import org.springframework.transaction.annotation.Transactional

@Service
class WineGateway(
    private val wineRepository: WineRepository,
    private val importerRepository: ImporterRepository
) {

    @Transactional(readOnly = true, propagation = MANDATORY)
    fun findByRegionUuid(uuid: String): List<BasePayloads.SimpleResponse> {
        return wineRepository.findByRegionUuid(uuid)
    }

    @Transactional(readOnly = true, propagation = MANDATORY)
    fun findByImporterUuid(uuid: String): List<BasePayloads.SimpleResponse> {
        return wineRepository.findByImporterUuid(uuid)
    }

    @Transactional(readOnly = true, propagation = MANDATORY)
    fun findByWineryUuid(uuid: String): List<BasePayloads.SimpleResponse> {
        return wineRepository.findByWineryUuid(uuid)
    }

    @Transactional(readOnly = true, propagation = MANDATORY)
    fun findById(id: Long): Wine {
        return wineRepository.findById(id)
            .orElseThrow { CommonApplicationException(MessageCode.NOT_FOUND_WINE) }
    }

    @Transactional(readOnly = true, propagation = MANDATORY)
    fun findByUuid(uuid: String): Wine {
        return wineRepository.findByUuid(uuid)
            .orElseThrow { CommonApplicationException(MessageCode.NOT_FOUND_WINE) }
    }

    @Transactional(readOnly = true, propagation = MANDATORY)
    fun findImporterByUuid(uuid: String): Importer {
        return importerRepository.findByUuid(uuid)
            .orElseThrow { CommonApplicationException(MessageCode.NOT_FOUND_IMPORTER) }
    }

    @Transactional(readOnly = true, propagation = MANDATORY)
    fun findImporterById(id: Long): Importer {
        return importerRepository.findById(id)
            .orElseThrow { CommonApplicationException(MessageCode.NOT_FOUND_IMPORTER) }
    }

}
