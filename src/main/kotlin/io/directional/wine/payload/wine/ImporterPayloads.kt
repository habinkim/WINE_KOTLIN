package io.directional.wine.payload.wine

import io.directional.wine.common.config.BasePayloads
import jakarta.validation.constraints.NotBlank

class ImporterPayloads {

    open class ListResponse(
        var uuid: String? = null,
        var name: String? = null
    )

    class DetailResponse(
        uuid: String?, name: String?,
        val wine: List<BasePayloads.SimpleResponse>? = ArrayList()
    ) : ListResponse(uuid, name)

    class CreateRequest (
        @NotBlank(message = "이름을 입력하지 않으셨습니다.") val name: String
    )

    class UpdateRequest (
        @NotBlank(message = "고유번호를 입력하지 않으셨습니다.") val uuid: String,
        @NotBlank(message = "이름을 입력하지 않으셨습니다.") val name: String
    )

}
