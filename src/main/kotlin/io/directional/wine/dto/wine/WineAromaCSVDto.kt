package io.directional.wine.dto.wine

import com.opencsv.bean.CsvBindByName

class WineAromaCSVDto {

    @CsvBindByName
    val name_korean: String? = null

    @CsvBindByName
    val name_english: String? = null

    @CsvBindByName
    val aroma : String? = null

}
