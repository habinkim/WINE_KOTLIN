package io.directional.wine.payload.grape

import com.fasterxml.jackson.annotation.JsonCreator
import io.directional.wine.common.config.BasePayloads
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import org.springframework.data.domain.Sort

class GrapePayloads {
    data class ListRequest(
        @Positive(message = "페이지 번호는 0 이상이어야 합니다") val pageNo: Int? = null,
        @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다") val pageSize: Int? = null,
        val sort: String? = null,
        val direction: Sort.Direction? = null,
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
                regionUuid: String? = null,
                nameEnglish: String?,
                nameKorean: String?
            ) = ListRequest(
                pageNo ?: 0,
                pageSize ?: 10,
                sort,
                direction ?: Sort.Direction.ASC,
                regionUuid,
                nameEnglish,
                nameKorean
            )
        }
    }

    open class SimpleResponse(
        uuid: String?, nameEnglish: String?, nameKorean: String?,
        open val region: List<BasePayloads.SimpleResponse>? = ArrayList()
    ) : BasePayloads.SimpleResponse(uuid, nameEnglish, nameKorean)

    class DetailResponse(
        uuid: String?, nameEnglish: String?, nameKorean: String?,
        acidity: Int? = null,
        body: Int? = null,
        sweetness: Int? = null,
        tannin: Int? = null,
        override val region: List<BasePayloads.SimpleResponse>? = ArrayList(),
        val wine: List<BasePayloads.SimpleResponse>? = ArrayList()
    ) : SimpleResponse(uuid, nameEnglish, nameKorean, region)

    data class CreateRequest(
        @NotBlank(message = "영어 이름을 입력하지 않으셨습니다.") val nameEnglish: String,
        @NotBlank(message = "한글 이름을 입력하지 않으셨습니다.") val nameKorean: String,
        @NotNull(message = "산도를 입력하지 않으셨습니다.") @Positive(message = "산도는 0 이상이어야 합니다") val acidity: Int,
        @NotNull(message = "바디감을 입력하지 않으셨습니다.") @Positive(message = "바디감은 0 이상이어야 합니다") val body: Int,
        @NotNull(message = "당도를 입력하지 않으셨습니다.") @Positive(message = "당도는 0 이상이어야 합니다") val sweetness: Int,
        @NotNull(message = "타닌을 입력하지 않으셨습니다.") @Positive(message = "타닌은 0 이상이어야 합니다") val tannin: Int
    )

    data class UpdateRequest(
        @NotBlank(message = "고유번호를 입력하지 않으셨습니다.") val uuid: String,
        val nameEnglish: String? = null,
        val nameKorean: String? = null,
        @Positive(message = "산도는 0 이상이어야 합니다") val acidity: Int? = null,
        @Positive(message = "바디감은 0 이상이어야 합니다") val body: Int? = null,
        @Positive(message = "당도는 0 이상이어야 합니다") val sweetness: Int? = null,
        @Positive(message = "타닌은 0 이상이어야 합니다") val tannin: Int? = null
    )

    data class CreateShareRequest(
        @NotBlank(message = "포도 고유번호를 입력하지 않으셨습니다.") val grapeUuid: String,
        @NotBlank(message = "포도 고유번호를 입력하지 않으셨습니다.") val regionUuid: String,
        @NotNull(message = "포도 품종 비율을 입력하지 않으셨습니다.") val share: Float
    )

}
