package io.directional.wine.base

import io.directional.wine.base.doc.RestDocsConfig
import io.directional.wine.common.util.StringUtils
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.context.annotation.Import
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.filter.CharacterEncodingFilter
import java.nio.charset.StandardCharsets

@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(RestDocsConfig::class)
@ExtendWith(RestDocumentationExtension::class)
abstract class ControllerBaseTest : AbstractIntegrationTest() {

    @Autowired
    protected lateinit var restDocs: RestDocumentationResultHandler

    @Autowired
    protected lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setUp(context: WebApplicationContext?, provider: RestDocumentationContextProvider?) {
        mockMvc = MockMvcBuilders.webAppContextSetup(context!!)
            .apply<DefaultMockMvcBuilder>(MockMvcRestDocumentation.documentationConfiguration(provider))
            .alwaysDo<DefaultMockMvcBuilder>(MockMvcResultHandlers.print())
            .alwaysDo<DefaultMockMvcBuilder>(restDocs)
            .addFilters<DefaultMockMvcBuilder>(CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
            .build()
    }
}
