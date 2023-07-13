package io.directional.wine.payload.winery

import com.fasterxml.jackson.annotation.JsonCreator
import io.directional.wine.common.config.BasePayloads
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive

class WineryPayloads {
    data class ListRequest(
        @Positive(message = "페이지 번호는 0 이상이어야 합니다") val pageNo: Int? = null,
        @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다") val pageSize: Int? = null,
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
                regionUuid: String?,
                nameEnglish: String?,
                nameKorean: String?
            ) = ListRequest(pageNo ?: 0, pageSize ?: 10, regionUuid, nameEnglish, nameKorean)
        }
    }

    class DetailResponse(
        uuid: String?, nameEnglish: String?, nameKorean: String?,
        region: BasePayloads.SimpleResponse,
        val wine: List<BasePayloads.SimpleResponse>? = ArrayList()
    ) : BasePayloads.SimpleResponse(uuid, nameEnglish, nameKorean)


    class CreateRequest(
        @NotBlank(message = "영어 이름을 입력하지 않으셨습니다.") val nameEnglish: String,
        @NotBlank(message = "한글 이름을 입력하지 않으셨습니다.") val nameKorean: String,
        @NotBlank(message = "지역 식별자를 입력하지 않으셨습니다.") val regionUuid: String
    )


    class UpdateRequest(
        @NotBlank(message = "식별자를 입력하지 않으셨습니다.") val uuid: String,
        val nameEnglish: String?,
        val nameKorean: String?,
        val regionUuid: String?
    )

}
