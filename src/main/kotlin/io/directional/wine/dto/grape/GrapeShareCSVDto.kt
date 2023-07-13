package io.directional.wine.dto.grape

import com.opencsv.bean.CsvBindByName

class GrapeShareCSVDto {

    @CsvBindByName
    val name_korean: String? = null

    @CsvBindByName
    val name_english: String? = null

    @CsvBindByName
    val share: String? = null

    @CsvBindByName
    val region_name_korean: String? = null

    @CsvBindByName
    val region_name_english:String?=null

}
