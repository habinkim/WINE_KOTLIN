package io.directional.wine.winery.mapper

import io.directional.wine.common.response.MessageCode
import io.directional.wine.domain.winery.Winery
import io.directional.wine.dto.winery.WineryCSVDto
import io.directional.wine.exception.CommonApplicationException
import io.directional.wine.payload.winery.WineryPayloads
import io.directional.wine.region.repository.RegionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Primary
import org.springframework.util.StringUtils

@Primary
abstract class WineryDecorator : WineryMapper {

    @Qualifier("delegate")
    @Autowired
    private lateinit var delegate: WineryMapper

    @Autowired
    private lateinit var regionRepository: RegionRepository

    override fun fromWineryCSVDto(dto: WineryCSVDto): Winery {
        var winery: Winery = delegate.fromWineryCSVDto(dto)

        if (StringUtils.hasText(dto.region_name_english)) {
            winery.region = regionRepository.findByNameEnglish(dto.region_name_english!!).orElseGet { null }
        }

        return winery
    }

    override fun fromWineryCSVDtoList(dtos: List<WineryCSVDto>): List<Winery> {
        val list: MutableList<Winery> = ArrayList(dtos.size)
        for (wineryCSVDto in dtos) {
            list.add(fromWineryCSVDto(wineryCSVDto))
        }
        return list
    }

    override fun fromCreateRequestDto(request: WineryPayloads.CreateRequest): Winery {
        var winery: Winery = delegate.fromCreateRequestDto(request)

        val region = regionRepository.findByUuid(request.regionUuid)
            .orElseThrow { CommonApplicationException(MessageCode.NOT_FOUND_REGION) }

        winery.region = region

        return winery
    }

    override fun fromUpdateRequestDto(request: WineryPayloads.UpdateRequest, winery: Winery): Winery {
        var updatedWinery: Winery = delegate.fromUpdateRequestDto(request, winery)

        if (StringUtils.hasText(request.regionUuid)) {
            updatedWinery.region = regionRepository.findByUuid(request.regionUuid!!)
                .orElseThrow { CommonApplicationException(MessageCode.NOT_FOUND_REGION) }
        }

        return winery
    }
}
