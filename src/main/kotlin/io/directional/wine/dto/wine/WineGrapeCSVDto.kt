package io.directional.wine.dto.wine

import com.opencsv.bean.CsvBindByName

class WineGrapeCSVDto {

    @CsvBindByName
    val name_korean: String? = null

    @CsvBindByName
    val name_english: String? = null

    @CsvBindByName
    val grape_name_korean: String? = null

    @CsvBindByName
    val grape_name_english: String? = null

}
