package io.directional.wine.region.mapper

import io.directional.wine.common.response.MessageCode
import io.directional.wine.domain.region.Region
import io.directional.wine.dto.region.RegionCSVDto
import io.directional.wine.exception.CommonApplicationException
import io.directional.wine.payload.region.RegionPayloads
import io.directional.wine.region.repository.RegionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Primary
import org.springframework.util.StringUtils

@Primary
abstract class RegionDecorator : RegionMapper {

    @Qualifier("delegate")
    @Autowired
    private lateinit var delegate: RegionMapper

    @Autowired
    private lateinit var regionRepository: RegionRepository

    override fun fromRegionCSVDto(dto: RegionCSVDto): Region {
        val region: Region = delegate.fromRegionCSVDto(dto)
        return setParent(dto, region)
    }

    override fun fromRegionCSVDtoList(dtos: List<RegionCSVDto>): List<Region> {
        val list: MutableList<Region> = ArrayList(dtos.size)
        for (regionCSVDto in dtos) {
            list.add(fromRegionCSVDto(regionCSVDto))
        }
        return list
    }

    override fun updateFromRegionCSVDto(dto: RegionCSVDto, region: Region): Region {
        return setParent(dto, region)
    }

    override fun fromCreateRequestDto(dto: RegionPayloads.CreateRequest): Region {
        val region = delegate.fromCreateRequestDto(dto)

        if (dto.parentUuid != null) {
            region.parent = regionRepository.findByUuid(dto.parentUuid)
                .orElseThrow { CommonApplicationException(MessageCode.NOT_FOUND_REGION) }
        }

        return region
    }

    override fun fromUpdateRequestDto(dto: RegionPayloads.UpdateRequest, region: Region): Region {
        val updatedRegion = delegate.fromUpdateRequestDto(dto, region)

        if (dto.parentUuid != null) {
            updatedRegion.parent = regionRepository.findByUuid(dto.parentUuid)
                .orElseThrow { CommonApplicationException(MessageCode.NOT_FOUND_REGION) }
        }

        return updatedRegion
    }

    private fun setParent(
        dto: RegionCSVDto,
        region: Region
    ): Region {
        if (StringUtils.hasText(dto.parent_name_english)) {
            region.parent = regionRepository.findByNameEnglish(dto.parent_name_english!!).orElseGet { null }
        }
        return region
    }

}
