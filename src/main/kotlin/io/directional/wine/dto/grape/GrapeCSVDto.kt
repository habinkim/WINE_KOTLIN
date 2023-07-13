package io.directional.wine.dto.grape

import com.opencsv.bean.CsvBindByName

class GrapeCSVDto {

    @CsvBindByName
    var name_korean: String? = null

    @CsvBindByName
    var name_english: String? = null

    @CsvBindByName
    var acidity: String? = null

    @CsvBindByName
    var body: String? = null

    @CsvBindByName
    var sweetness: String? = null

    @CsvBindByName
    var tannin: String? = null

}
