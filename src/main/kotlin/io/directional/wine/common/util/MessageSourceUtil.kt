package io.directional.wine.common.util

import io.directional.wine.common.response.MessageCode
import org.springframework.context.MessageSource
import org.springframework.stereotype.Component
import java.util.*

@Component
class MessageSourceUtil(
    private val messageSource: MessageSource
) {

    fun getMessage(code: String): String {
        return messageSource.getMessage(code, null, Locale.getDefault())
    }

    fun getMessage(messageCode: MessageCode): String {
        return getMessage(messageCode.code)
    }

    fun getMessage(code: String, locale: Locale): String {
        return messageSource.getMessage(code, null, locale)
    }

}
