package io.directional.wine.base

import io.directional.wine.WineApplication
import io.directional.wine.common.util.StringUtils
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [WineApplication::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
abstract class AbstractIntegrationTest {

    @Autowired
    protected lateinit var stringUtils: StringUtils

}
