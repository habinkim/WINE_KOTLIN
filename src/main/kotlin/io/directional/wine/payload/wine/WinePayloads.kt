package io.directional.wine.payload.wine

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import io.directional.wine.common.config.BasePayloads
import io.directional.wine.common.enums.WineTagType
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import org.springframework.data.domain.Sort
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class WinePayloads {
    data class ListRequest(
        @Positive(message = "페이지 번호는 0 이상이어야 합니다") val pageNo: Int? = null,
        @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다") val pageSize: Int? = null,
        val sort: String? = null,
        val direction: Sort.Direction? = null,
        val type: String? = null,
        val minAlcohol: Float? = null,
        val maxAlcohol: Float? = null,
        val minPrice: Int? = null,
        val maxPrice: Int? = null,
        val style: String? = null,
        val grade: String? = null,
        val regionUuid: String? = null,
        val nameEnglish: String? = null,
        val nameKorean: String? = null
    ) {
        companion object {
            @JsonCreator
            @JvmStatic
            operator fun invoke(
                pageNo: Int?,
                pageSize: Int?,
                sort: String? = null,
                direction: Sort.Direction? = null,
                type: String? = null,
                minAlcohol: Float? = null,
                maxAlcohol: Float? = null,
                minPrice: Int? = null,
                maxPrice: Int? = null,
                style: String? = null,
                grade: String? = null,
                regionUuid: String? = null,
                nameEnglish: String? = null,
                nameKorean: String? = null
            ) = ListRequest(
                pageNo ?: 0,
                pageSize ?: 10,
                sort,
                direction ?: Sort.Direction.ASC,
                type,
                minAlcohol,
                maxAlcohol,
                minPrice,
                maxPrice,
                style,
                grade,
                regionUuid,
                nameEnglish,
                nameKorean
            )
        }
    }

    class ListResponse(
        val uuid: String? = null,
        val nameEnglish: String? = null,
        val nameKorean: String? = null,
        val type: String? = null,

        @field:JsonIgnore
        val regionUuid: String? = null,

        var rootRegion: BasePayloads.SimpleResponse? = null
    )

    class DetailResponse(
        var uuid: String? = null,
        var nameEnglish: String? = null,
        var nameKorean: String? = null,
        var type: String? = null,

        @field:JsonIgnore
        val regionUuid: String? = null,

        var rootRegion: BasePayloads.SimpleResponse? = null,
        var regions: List<BasePayloads.SimpleResponse>? = ArrayList(),
        var alcohol: Float? = null,
        var acidity: Int? = null,
        var body: Int? = null,
        var sweetness: Int? = null,
        var tannin: Int? = null,
        var servingTemperature: Float? = null,
        var score: Float? = null,
        var price: Int? = null,
        var style: String? = null,
        var grade: String? = null,
        var importer: ImporterPayloads.ListResponse? = null,
        var winery: BasePayloads.SimpleResponse? = null,
        var wineryRegion: BasePayloads.SimpleResponse? = null,
        var tags: Map<WineTagType, List<String>>? = EnumMap(WineTagType::class.java),
        var grapes: List<BasePayloads.SimpleResponse>? = ArrayList()
    )

    class CreateRequest(
        @NotBlank(message = "영어 이름을 입력하지 않으셨습니다.") val nameEnglish: String?,
        @NotBlank(message = "한글 이름을 입력하지 않으셨습니다.") val nameKorean: String?,

        @NotBlank(message = "타입을 입력하지 않으셨습니다.") val type: String?,
        @NotNull(message = "도수를 입력하지 않으셨습니다.") val alcohol: Float?,
        @NotNull(message = "산도를 입력하지 않으셨습니다.") val acidity: Int?,
        @NotNull(message = "바디감을 입력하지 않으셨습니다.") val body: Int?,
        @NotNull(message = "당도를 입력하지 않으셨습니다.") val sweetness: Int?,
        @NotNull(message = "타닌을 입력하지 않으셨습니다.") val tannin: Int?,
        @NotNull(message = "권장 섭취온도를 입력하지 않으셨습니다.") val servingTemperature: Float?,
        @NotNull(message = "점수를 입력하지 않으셨습니다.") val score: Float?,
        @NotNull(message = "가격을 입력하지 않으셨습니다.") var price: Int?,
        @NotBlank(message = "스타일을 입력하지 않으셨습니다.") var style: String?,
        @NotBlank(message = "등급을 입력하지 않으셨습니다.") var grade: String?,

        @NotBlank(message = "수입사 식별자를 입력하지 않으셨습니다.") val importerUuid: String?,
        @NotBlank(message = "와이너리 식별자를 입력하지 않으셨습니다.") val wineryUuid: String?,
        @NotBlank(message = "지역 식별자를 입력하지 않으셨습니다.") val regionUuid: String?
    )

    class UpdateRequest(
        @NotBlank(message = "식별자를 입력하지 않으셨습니다.") val uuid: String,
        val nameEnglish: String?,
        val nameKorean: String?,

        val type: String?,
        val alcohol: Float?,
        val acidity: Int?,
        val body: Int?,
        val sweetness: Int?,
        val tannin: Int?,
        val servingTemperature: Float?,
        val score: Float?,
        var price: Int?,
        var style: String?,
        var grade: String?,

        val importerUuid: String?,
        val wineryUuid: String?,
        val regionUuid: String?
    )

}
