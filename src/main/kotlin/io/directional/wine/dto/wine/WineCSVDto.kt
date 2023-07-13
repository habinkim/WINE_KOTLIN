package io.directional.wine.dto.wine

import com.opencsv.bean.CsvBindByName

class WineCSVDto {

    @CsvBindByName
    val type: String? = null

    @CsvBindByName
    val name_korean: String? = null

    @CsvBindByName
    val name_english: String? = null

    @CsvBindByName
    val alcohol: String? = null

    @CsvBindByName
    val acidity: String? = null

    @CsvBindByName
    val body: String? = null

    @CsvBindByName
    val sweetness: String? = null

    @CsvBindByName
    val tannin: String? = null

    @CsvBindByName
    val serving_temperature: Float? = null

    @CsvBindByName
    val score: Float? = null

    @CsvBindByName
    val price: String? = null

    @CsvBindByName
    val style: String? = null

    @CsvBindByName
    val grade: String? = null

    @CsvBindByName
    val importer: String? = null

    @CsvBindByName
    val winery_name_korean: String? = null

    @CsvBindByName
    val winery_name_english: String? = null

    @CsvBindByName
    val region_name_korean: String? = null

    @CsvBindByName
    val region_name_english: String? = null

}
