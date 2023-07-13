package io.directional.wine.base.doc

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler

@TestConfiguration
@Profile("test")
class RestDocsConfig {
    @Bean
    fun write(): RestDocumentationResultHandler {
        return MockMvcRestDocumentation.document(
            "{method-name}",
            DocumentUtils.documentRequest,
            DocumentUtils.documentResponse
        )
    }

}
