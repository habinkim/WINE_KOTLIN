package io.directional.wine.common.response

import io.directional.wine.common.util.MessageSourceUtil
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class ResponseMapper(
    private val messageSourceUtil: MessageSourceUtil
) {

    fun ok(): ResponseEntity<BaseResponse<Nothing?>> {
        return ResponseEntity.ok().body(BaseResponse(MessageCode.SUCCESS.name, null))
    }

    private fun <T> ok(messageCode: MessageCode, source: T): ResponseEntity<BaseResponse<T>> {
        return ResponseEntity.ok().body(BaseResponse(messageCode.name, source))
    }

    fun <T> ok(source: T): ResponseEntity<BaseResponse<T>> {
        return ok(MessageCode.SUCCESS, source)
    }

    fun error(messageCode: MessageCode): ResponseEntity<BaseExceptionResponse> {
        return ResponseEntity
            .status(messageCode.httpStatus)
            .body(
                BaseExceptionResponse(
                    messageSourceUtil.getMessage(messageCode.code),
                    messageCode.code
                )
            )
    }

}
