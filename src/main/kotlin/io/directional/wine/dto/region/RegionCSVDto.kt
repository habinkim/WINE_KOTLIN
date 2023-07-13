package io.directional.wine.dto.region

import com.opencsv.bean.CsvBindByName

class RegionCSVDto {

    @CsvBindByName
    val name_korean: String? = null

    @CsvBindByName
    val name_english: String? = null

    @CsvBindByName
    val parent_name_korean: String? = null

    @CsvBindByName
    val parent_name_english: String? = null


}
