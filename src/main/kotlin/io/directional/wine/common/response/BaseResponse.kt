package io.directional.wine.common.response

import com.fasterxml.jackson.annotation.JsonInclude

data class BaseResponse<T>(
    val message: String, @field:JsonInclude(JsonInclude.Include.NON_NULL) @param:JsonInclude(
        JsonInclude.Include.NON_ABSENT
    ) val result: T
)
