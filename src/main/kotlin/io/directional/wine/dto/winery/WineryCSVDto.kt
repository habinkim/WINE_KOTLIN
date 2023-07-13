package io.directional.wine.dto.winery

import com.opencsv.bean.CsvBindByName

class WineryCSVDto {

    @CsvBindByName
    val name_korean: String? = null

    @CsvBindByName
    val name_english: String? = null

    @CsvBindByName
    val region_name_korean: String? = null

    @CsvBindByName
    val region_name_english: String? = null

}
