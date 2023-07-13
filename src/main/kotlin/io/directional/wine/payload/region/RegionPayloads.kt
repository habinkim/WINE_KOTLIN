package io.directional.wine.payload.region

import com.fasterxml.jackson.annotation.JsonCreator
import io.directional.wine.common.config.BasePayloads
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive

class RegionPayloads {
    data class ListRequest(
        @Positive(message = "페이지 번호는 0 이상이어야 합니다") val pageNo: Int? = null,
        @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다") val pageSize: Int? = null,
        val parentUuid: String? = null,
        val nameEnglish: String? = null,
        val nameKorean: String? = null
    ) {
        companion object {
            @JsonCreator
            @JvmStatic
            operator fun invoke(
                pageNo: Int?,
                pageSize: Int?,
                parentUuid: String?,
                nameEnglish: String?,
                nameKorean: String?
            ) = ListRequest(pageNo ?: 0, pageSize ?: 10, parentUuid, nameEnglish, nameKorean)
        }
    }

    class DetailResponse(
        uuid: String?,
        nameEnglish: String?,
        nameKorean: String?,
        val parent: List<BasePayloads.SimpleResponse>? = ArrayList(),
        val grape: List<BasePayloads.SimpleResponse>? = ArrayList(),
        val winery: List<BasePayloads.SimpleResponse>? = ArrayList(),
        val wine: List<BasePayloads.SimpleResponse>? = ArrayList()

    ) : BasePayloads.SimpleResponse(uuid, nameEnglish, nameKorean)

    class CreateRequest(
        @NotBlank(message = "영어 이름을 입력하지 않으셨습니다.") val nameEnglish: String,
        @NotBlank(message = "한글 이름을 입력하지 않으셨습니다.") val nameKorean: String,
        val parentUuid: String? = null
    )

    class UpdateRequest(
        @NotBlank(message = "고유번호를 입력하지 않으셨습니다.") val uuid: String,
        val nameEnglish: String? = null,
        val nameKorean: String? = null,
        val parentUuid: String? = null
    )

}
