package io.directional.wine.common.util

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component

@Component
class StringUtils(
    private val objectMapper: ObjectMapper
) {

    @Throws(JsonProcessingException::class)
    fun <T> toJson(data: T): String {
        return objectMapper.writeValueAsString(data)
    }

    @Throws(JsonProcessingException::class)
    fun <T> toPrettyJson(data: T): String {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(data)
    }

}
