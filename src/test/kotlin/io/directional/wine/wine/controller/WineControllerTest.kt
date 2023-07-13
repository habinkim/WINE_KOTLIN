package io.directional.wine.wine.controller

import io.directional.wine.base.ControllerBaseTest
import io.directional.wine.common.config.Uris
import io.directional.wine.common.config.logger
import io.directional.wine.common.response.MessageCode
import io.directional.wine.domain.region.Region
import io.directional.wine.domain.wine.Importer
import io.directional.wine.exception.CommonApplicationException
import io.directional.wine.payload.wine.ImporterPayloads
import io.directional.wine.payload.wine.WinePayloads
import io.directional.wine.region.service.RegionGateway
import io.directional.wine.wine.repository.ImporterRepository
import io.directional.wine.wine.repository.WineRepository
import io.directional.wine.wine.service.WineGateway
import io.directional.wine.winery.service.WineryGateway
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

class WineControllerTest : ControllerBaseTest() {

    @Autowired
    private lateinit var wineGateway: WineGateway

    @Autowired
    private lateinit var wineryGateway: WineryGateway

    @Autowired
    private lateinit var regionGateway: RegionGateway

    @Autowired
    private lateinit var wineRepository: WineRepository

    @Autowired
    private lateinit var importerRepository: ImporterRepository

    private val log = logger()

    @Transactional
    @Test
    @Order(1)
    @DisplayName("수입사 리스트 조회, 성공")
    fun importerListSuccess() {

        val uri = Uris.IMPORTER_ROOT

        mockMvc.perform(
            get(uri)
                .queryParam("name", "가자")
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message", notNullValue()))
            .andExpect(jsonPath("$.result[0]", notNullValue()))
            .andExpect(jsonPath("$.result[0].uuid", notNullValue()))
            .andExpect(jsonPath("$.result[0].name", notNullValue()))

            .andExpect(jsonPath("$.message", `is`(MessageCode.SUCCESS.name)))
            .andExpect(
                jsonPath(
                    "$.result[0].uuid",
                    matchesPattern("([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})")
                )
            )
            .andExpect(jsonPath("$.result[0].name", `is`("가자주류")))
            .andDo(
                restDocs.document(
                    queryParameters(
                        parameterWithName("name").description("수입사명").optional()
                    ),
                    responseFields(
                        fieldWithPath("message").description("시스템 메시지"),
                        fieldWithPath("result[]").description("오브젝트"),
                        fieldWithPath("result[].uuid").description("수입사 식별자"),
                        fieldWithPath("result[].name").description("수입사명"),
                    )
                )
            )

    }

    @Transactional
    @Test
    @Order(2)
    @DisplayName("수입사 상세 조회, 성공")
    fun importerDetailSuccess() {

        val importer: Importer = wineGateway.findImporterById(12)
        val uuid = importer.uuid

        val uri = Uris.IMPORTER_ROOT + Uris.REST_NAME_UUID

        mockMvc.perform(
            get(uri, uuid)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message", notNullValue()))
            .andExpect(jsonPath("$.result", notNullValue()))
            .andExpect(jsonPath("$.result.uuid", notNullValue()))
            .andExpect(jsonPath("$.result.name", notNullValue()))

            .andExpect(jsonPath("$.result.wine[0]", notNullValue()))
            .andExpect(jsonPath("$.result.wine[0].uuid", notNullValue()))
            .andExpect(jsonPath("$.result.wine[0].nameEnglish", notNullValue()))
            .andExpect(jsonPath("$.result.wine[0].nameKorean", notNullValue()))

            .andExpect(jsonPath("$.message", `is`(MessageCode.SUCCESS.name)))
            .andExpectAll(
                jsonPath(
                    "$.result.uuid",
                    matchesPattern("([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})")
                ), jsonPath("$.result.uuid", `is`(uuid))
            )
            .andExpect(jsonPath("$.result.name", `is`(importer.name)))

            .andExpect(jsonPath("$.result.wine[0].uuid", matchesPattern("([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})")))
            .andExpect(jsonPath("$.result.wine[0].nameEnglish", `is`("Monte del Fra, Ca del Magro Custoza Superiore")))
            .andExpect(jsonPath("$.result.wine[0].nameKorean", `is`("몬테 델 프라, 까델마그로 쿠스토자 수페리오레")))
            .andDo(
                restDocs.document(
                    pathParameters(
                        parameterWithName("uuid").description("수입사 식별자").optional()
                    ),
                    responseFields(
                        fieldWithPath("message").description("시스템 메시지"),
                        fieldWithPath("result").description("오브젝트"),
                        fieldWithPath("result.uuid").description("수입사 식별자"),
                        fieldWithPath("result.name").description("수입사명"),
                        fieldWithPath("result.wine[]").description("와인"),
                        fieldWithPath("result.wine[].uuid").description("와인 식별자"),
                        fieldWithPath("result.wine[].nameEnglish").description("와인 이름(영어)"),
                        fieldWithPath("result.wine[].nameKorean").description("와인 이름(한글)")
                    )
                )
            )

    }

    @Transactional
    @Test
    @Order(3)
    @DisplayName("수입사 생성, 성공")
    fun importerCreateSuccess() {

        val uri = Uris.IMPORTER_ROOT
        val name = "하빈주류"
        val request = ImporterPayloads.CreateRequest(name)

        val resultActions = mockMvc.perform(
            post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(stringUtils.toJson(request))
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message", notNullValue()))
            .andExpect(jsonPath("$.message", `is`(MessageCode.SUCCESS.name)))

        assertDoesNotThrow {
            importerRepository.findByName(name)
                .orElseThrow { CommonApplicationException(MessageCode.NOT_FOUND_IMPORTER) }
        }

        resultActions
            .andDo(
                restDocs.document(
                    requestFields(
                        fieldWithPath("name").description("수입사명")

                    ),
                    responseFields(
                        fieldWithPath("message").description("시스템 메시지")
                    )
                )
            )

    }

    @Transactional
    @Test
    @Order(4)
    @DisplayName("수입사 수정, 성공")
    fun importerUpdateSuccess() {

        val uri = Uris.IMPORTER_ROOT

        val importer: Importer = wineGateway.findImporterById(12)
        val uuid = importer.uuid!!
        val name = "하빈주류"

        val request = ImporterPayloads.UpdateRequest(uuid, name)

        val resultActions = mockMvc.perform(
            patch(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(stringUtils.toJson(request))
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message", notNullValue()))
            .andExpect(jsonPath("$.message", `is`(MessageCode.SUCCESS.name)))

        val updatedImporter = wineGateway.findImporterByUuid(uuid)
        assertEquals(updatedImporter.uuid, uuid)
        assertEquals(updatedImporter.name, name)

        resultActions.andDo(
            restDocs.document(
                requestFields(
                    fieldWithPath("uuid").description("수입사 식별자"),
                    fieldWithPath("name").description("수입사명")
                ),
                responseFields(
                    fieldWithPath("message").description("시스템 메시지")
                )
            )
        )

    }

    @Transactional
    @Test
    @Order(5)
    @DisplayName("수입사 삭제, 성공")
    fun importerDeleteSuccess() {

        val uri = Uris.IMPORTER_ROOT + Uris.REST_NAME_UUID

        val importer: Importer = wineGateway.findImporterById(12)
        val uuid = importer.uuid!!

        val resultActions = mockMvc.perform(
            delete(uri, uuid)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message", notNullValue()))
            .andExpect(jsonPath("$.message", `is`(MessageCode.SUCCESS.name)))

        assertThrows(CommonApplicationException::class.java) { wineGateway.findImporterByUuid(uuid) }

        resultActions.andDo(
            restDocs.document(
                pathParameters(
                    parameterWithName("uuid").description("수입사 식별자")
                ),
                responseFields(
                    fieldWithPath("message").description("시스템 메시지")
                )
            )
        )

    }

    @Transactional
    @Test
    @Order(6)
    @DisplayName("와인 리스트 조회, 성공")
    fun wineListSuccess() {

        val uri = Uris.WINE_ROOT + "/list"

        val request = WinePayloads.ListRequest(
            0, null, "body", null, null,
            13.0F, 15.0F, 700000,
            null, null, null, null, null, null
        )

        mockMvc.perform(
            post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(stringUtils.toJson(request))
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message", notNullValue()))
            .andExpect(jsonPath("$.result.content[0]", notNullValue()))
            .andExpect(jsonPath("$.result.content[0].uuid", notNullValue()))
            .andExpect(jsonPath("$.result.content[0].nameEnglish", notNullValue()))
            .andExpect(jsonPath("$.result.content[0].nameKorean", notNullValue()))
            .andExpect(jsonPath("$.result.content[0].type", notNullValue()))

            .andExpect(jsonPath("$.result.content[0].rootRegion", notNullValue()))
            .andExpect(jsonPath("$.result.content[0].rootRegion.uuid", notNullValue()))
            .andExpect(jsonPath("$.result.content[0].rootRegion.nameEnglish", notNullValue()))
            .andExpect(jsonPath("$.result.content[0].rootRegion.nameKorean", notNullValue()))

            .andExpect(jsonPath("$.message", `is`(MessageCode.SUCCESS.name)))

            .andExpect(
                jsonPath(
                    "$.result.content[0].uuid", matchesPattern("([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})")
                )
            )
            .andExpect(jsonPath("$.result.content[0].nameEnglish", `is`("Bass Phillip, Reserve Pinot Noir")))
            .andExpect(jsonPath("$.result.content[0].nameKorean", `is`("바스 필립, 리저브 피노 누아")))
            .andExpect(jsonPath("$.result.content[0].type", `is`("RED")))

            .andExpect(
                jsonPath(
                    "$.result.content[0].rootRegion.uuid",
                    matchesPattern("([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})")
                )
            )
            .andExpect(jsonPath("$.result.content[0].rootRegion.nameEnglish", `is`("Australia")))
            .andExpect(jsonPath("$.result.content[0].rootRegion.nameKorean", `is`("호주")))


            .andExpect(jsonPath("$.result.pageable.offset", `is`(0)))
            .andExpect(jsonPath("$.result.pageable.pageNumber", `is`(0)))
            .andExpect(jsonPath("$.result.pageable.pageSize", `is`(10)))
            .andExpect(jsonPath("$.result.pageable.paged", `is`(true)))
            .andExpect(jsonPath("$.result.pageable.unpaged", `is`(false)))

            .andExpect(jsonPath("$.result.pageable.sort.empty", `is`(false)))
            .andExpect(jsonPath("$.result.pageable.sort.unsorted", `is`(false)))
            .andExpect(jsonPath("$.result.pageable.sort.sorted", `is`(true)))

            .andExpect(jsonPath("$.result.last", `is`(false)))
            .andExpect(jsonPath("$.result.totalPages", `is`(2)))
            .andExpect(jsonPath("$.result.totalElements", `is`(16)))
            .andExpect(jsonPath("$.result.first", `is`(true)))
            .andExpect(jsonPath("$.result.size", `is`(10)))
            .andExpect(jsonPath("$.result.number", `is`(0)))
            .andExpect(jsonPath("$.result.sort.empty", `is`(false)))
            .andExpect(jsonPath("$.result.sort.unsorted", `is`(false)))
            .andExpect(jsonPath("$.result.sort.sorted", `is`(true)))
            .andExpect(jsonPath("$.result.numberOfElements", `is`(10)))
            .andExpect(jsonPath("$.result.empty", `is`(false)))

            .andDo(
                restDocs.document(
                    requestFields(
                        fieldWithPath("pageNo").type(JsonFieldType.NUMBER).description("페이지 번호").optional(),
                        fieldWithPath("pageSize").type(JsonFieldType.NUMBER).description("페이지 당 크기").optional(),
                        fieldWithPath("sort").type(JsonFieldType.STRING)
                            .description("정렬 기준 필드 [nameEnglish, nameKorean, alcohol, acidity, body, sweetness, tannin, grade, price]")
                            .optional(),
                        fieldWithPath("direction").type(JsonFieldType.STRING).description("정렬 방향 [ASC, DESC]")
                            .optional(),
                        fieldWithPath("type").type(JsonFieldType.STRING).description("와인의 종류").optional(),
                        fieldWithPath("minAlcohol").type(JsonFieldType.NUMBER).description("최소 도수").optional(),
                        fieldWithPath("maxAlcohol").type(JsonFieldType.NUMBER).description("최대 도수").optional(),
                        fieldWithPath("minPrice").type(JsonFieldType.NUMBER).description("최소 가격").optional(),
                        fieldWithPath("maxPrice").type(JsonFieldType.NUMBER).description("최대 가격").optional(),
                        fieldWithPath("style").type(JsonFieldType.STRING).description("스타일").optional(),
                        fieldWithPath("grade").type(JsonFieldType.STRING).description("와인의 등급").optional(),
                        fieldWithPath("regionUuid").type(JsonFieldType.STRING).description("지역 식별자").optional(),
                        fieldWithPath("nameEnglish").type(JsonFieldType.STRING).description("포도 이름(영어)").optional(),
                        fieldWithPath("nameKorean").type(JsonFieldType.STRING).description("포도 이름(한글)").optional()
                    ),
                    responseFields(
                        fieldWithPath("message").description("시스템 메시지"),
                        fieldWithPath("result.content[]").description("오브젝트"),
                        fieldWithPath("result.content[].uuid").description("포도 고유번호"),
                        fieldWithPath("result.content[].nameEnglish").description("포도 이름(영어)"),
                        fieldWithPath("result.content[].nameKorean").description("포도 이름(한글)"),
                        fieldWithPath("result.content[].type").description("와인의 종류"),

                        fieldWithPath("result.content[].rootRegion").description("최상위 지역").optional(),
                        fieldWithPath("result.content[].rootRegion.uuid").description("최상위 지역 고유번호"),
                        fieldWithPath("result.content[].rootRegion.nameEnglish").description("최상위 지역 이름(영어)"),
                        fieldWithPath("result.content[].rootRegion.nameKorean").description("최상위 지역 이름(한글)"),


                        fieldWithPath("result.pageable").description("페이징 오브젝트"),
                        fieldWithPath("result.pageable.offset").description("offset"),
                        fieldWithPath("result.pageable.pageNumber").description("페이지 번호 (0부터 시작)"),
                        fieldWithPath("result.pageable.pageSize").description("요청한 페이지 당 크기"),
                        fieldWithPath("result.pageable.paged").description("페이징 여부"),
                        fieldWithPath("result.pageable.unpaged").description("미 페이징 여부"),
                        fieldWithPath("result.pageable.sort").description("정렬 정보"),
                        fieldWithPath("result.pageable.sort.empty").description("정렬 정보 미존재 여부"),
                        fieldWithPath("result.pageable.sort.unsorted").description("미정렬 여부"),
                        fieldWithPath("result.pageable.sort.sorted").description("정렬 여부"),

                        fieldWithPath("result.last").description("마지막 페이지 여부"),
                        fieldWithPath("result.totalPages").description("페이지 갯수"),
                        fieldWithPath("result.totalElements").description("총 요소 갯수"),
                        fieldWithPath("result.first").description("첫 페이지 여부"),
                        fieldWithPath("result.size").description("페이지 당 크기"),
                        fieldWithPath("result.number").description("페이지 번호 (0부터 시작)"),
                        fieldWithPath("result.sort.empty").description("정렬 정보 미존재 여부"),
                        fieldWithPath("result.sort.unsorted").description("미정렬 여부"),
                        fieldWithPath("result.sort.sorted").description("정렬 여부"),
                        fieldWithPath("result.numberOfElements").description("현재 페이지 요소 갯수"),
                        fieldWithPath("result.empty").description("현재 페이지 요소 미존재 여부")
                    )
                )
            )


    }

    @Transactional
    @Test
    @Order(7)
    @DisplayName("와인 상세 조회, 성공")
    fun wineDetailSuccess() {

        val uri = Uris.WINE_ROOT + Uris.REST_NAME_UUID

        val wine = wineGateway.findById(24)

        mockMvc.perform(
            get(uri, wine.uuid)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message", notNullValue()))
            .andExpect(jsonPath("$.result", notNullValue()))
            .andExpect(jsonPath("$.result.uuid", notNullValue()))
            .andExpect(jsonPath("$.result.nameEnglish", notNullValue()))
            .andExpect(jsonPath("$.result.nameKorean", notNullValue()))
            .andExpect(jsonPath("$.result.type", notNullValue()))
            .andExpect(jsonPath("$.result.alcohol", notNullValue()))
            .andExpect(jsonPath("$.result.acidity", notNullValue()))
            .andExpect(jsonPath("$.result.body", notNullValue()))
            .andExpect(jsonPath("$.result.sweetness", notNullValue()))
            .andExpect(jsonPath("$.result.tannin", notNullValue()))
            .andExpect(jsonPath("$.result.servingTemperature", notNullValue()))
            .andExpect(jsonPath("$.result.score", notNullValue()))
            .andExpect(jsonPath("$.result.price", notNullValue()))
            .andExpect(jsonPath("$.result.style", notNullValue()))
            .andExpect(jsonPath("$.result.grade", nullValue()))

            .andExpect(jsonPath("$.result.rootRegion.uuid", notNullValue()))
            .andExpect(jsonPath("$.result.rootRegion.nameEnglish", notNullValue()))
            .andExpect(jsonPath("$.result.rootRegion.nameKorean", notNullValue()))

            .andExpect(jsonPath("$.result.regions[0].uuid", notNullValue()))
            .andExpect(jsonPath("$.result.regions[0].nameEnglish", notNullValue()))
            .andExpect(jsonPath("$.result.regions[0].nameKorean", notNullValue()))

            .andExpect(jsonPath("$.result.importer.uuid", notNullValue()))
            .andExpect(jsonPath("$.result.importer.name", notNullValue()))

            .andExpect(jsonPath("$.result.winery.uuid", notNullValue()))
            .andExpect(jsonPath("$.result.winery.nameEnglish", notNullValue()))
            .andExpect(jsonPath("$.result.winery.nameKorean", notNullValue()))

            .andExpect(jsonPath("$.result.wineryRegion.uuid", notNullValue()))
            .andExpect(jsonPath("$.result.wineryRegion.nameEnglish", notNullValue()))
            .andExpect(jsonPath("$.result.wineryRegion.nameKorean", notNullValue()))

            .andExpect(jsonPath("$.result.tags.AROMA[0]", notNullValue()))
            .andExpect(jsonPath("$.result.tags.PAIRING[0]", notNullValue()))

            .andExpect(jsonPath("$.result.grapes[0].uuid", notNullValue()))
            .andExpect(jsonPath("$.result.grapes[0].nameEnglish", notNullValue()))
            .andExpect(jsonPath("$.result.grapes[0].nameKorean", notNullValue()))


            .andExpect(jsonPath("$.message", `is`(MessageCode.SUCCESS.name)))
            .andExpect(jsonPath("$.result.uuid", matchesPattern("([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})")))
            .andExpect(jsonPath("$.result.nameEnglish", `is`("Bass Phillip, Reserve Pinot Noir")))
            .andExpect(jsonPath("$.result.nameKorean", `is`("바스 필립, 리저브 피노 누아")))
            .andExpect(jsonPath("$.result.type", `is`("RED")))
            .andExpect(jsonPath("$.result.alcohol", `is`(14.5)))
            .andExpect(jsonPath("$.result.acidity", `is`(4)))
            .andExpect(jsonPath("$.result.body", `is`(3)))
            .andExpect(jsonPath("$.result.sweetness", `is`(1)))
            .andExpect(jsonPath("$.result.tannin", `is`(3)))
            .andExpect(jsonPath("$.result.servingTemperature", `is`(17.0)))
            .andExpect(jsonPath("$.result.score", `is`(95.0)))
            .andExpect(jsonPath("$.result.price", `is`(1300000)))
            .andExpect(jsonPath("$.result.style", `is`("Australian Pinot Noir")))

            .andExpect(
                jsonPath(
                    "$.result.rootRegion.uuid",
                    matchesPattern("([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})")
                )
            )
            .andExpect(jsonPath("$.result.rootRegion.nameEnglish", `is`("Australia")))
            .andExpect(jsonPath("$.result.rootRegion.nameKorean", `is`("호주")))

            .andExpect(
                jsonPath(
                    "$.result.regions[0].uuid",
                    matchesPattern("([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})")
                )
            )
            .andExpect(jsonPath("$.result.regions[0].nameEnglish", `is`("Gippsland")))
            .andExpect(jsonPath("$.result.regions[0].nameKorean", `is`("깁스랜드")))

            .andExpect(jsonPath("$.result.importer.uuid", matchesPattern("([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})")))
            .andExpect(jsonPath("$.result.importer.name", `is`("신세계엘앤비")))

            .andExpect(jsonPath("$.result.winery.uuid", matchesPattern("([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})")))
            .andExpect(jsonPath("$.result.winery.nameEnglish", `is`("Bass Phillip")))
            .andExpect(jsonPath("$.result.winery.nameKorean", `is`("바스 필립")))

            .andExpect(
                jsonPath(
                    "$.result.wineryRegion.uuid",
                    matchesPattern("([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})")
                )
            )
            .andExpect(jsonPath("$.result.wineryRegion.nameEnglish", `is`("Australia")))
            .andExpect(jsonPath("$.result.wineryRegion.nameKorean", `is`("호주")))

            .andExpect(jsonPath("$.result.tags.AROMA[0]", `is`("허브")))
            .andExpect(jsonPath("$.result.tags.PAIRING[0]", `is`("버섯")))

            .andExpect(jsonPath("$.result.grapes[0].uuid", matchesPattern("([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})")))
            .andExpect(jsonPath("$.result.grapes[0].nameEnglish", `is`("Pinot Noir")))
            .andExpect(jsonPath("$.result.grapes[0].nameKorean", `is`("피노 누아")))

            .andDo(
                restDocs.document(
                    pathParameters(
                        parameterWithName("uuid").description("와인 식별자")
                    ),
                    responseFields(
                        fieldWithPath("message").description("시스템 메시지"),
                        fieldWithPath("result").description("오브젝트"),
                        fieldWithPath("result.uuid").description("와인 식별자"),
                        fieldWithPath("result.nameEnglish").description("와인 이름(영어)"),
                        fieldWithPath("result.nameKorean").description("와인 이름(한글)"),
                        fieldWithPath("result.type").description("와인 타입"),
                        fieldWithPath("result.alcohol").description("도수"),
                        fieldWithPath("result.acidity").description("산도"),
                        fieldWithPath("result.body").description("바디감"),
                        fieldWithPath("result.sweetness").description("당도"),
                        fieldWithPath("result.tannin").description("타닌"),
                        fieldWithPath("result.servingTemperature").description("권장 섭취 온도"),
                        fieldWithPath("result.score").description("와인의 점수"),
                        fieldWithPath("result.price").description("와인의 가격"),
                        fieldWithPath("result.style").description("와인의 스타일"),
                        fieldWithPath("result.grade").description("와인의 등급"),

                        fieldWithPath("result.rootRegion").description("최상위 지역 오브젝트"),
                        fieldWithPath("result.rootRegion.uuid").description("최상위 지역 식별자"),
                        fieldWithPath("result.rootRegion.nameEnglish").description("최상위 지역 이름(영어)"),
                        fieldWithPath("result.rootRegion.nameKorean").description("최상위 지역 이름(한글)"),

                        fieldWithPath("result.regions[]").description("모든 지역 오브젝트"),
                        fieldWithPath("result.regions[].uuid").description("지역 식별자"),
                        fieldWithPath("result.regions[].nameEnglish").description("지역 이름(영어)"),
                        fieldWithPath("result.regions[].nameKorean").description("지역 이름(한글)"),

                        fieldWithPath("result.importer").description("수입사 오브젝트"),
                        fieldWithPath("result.importer.uuid").description("수입사 식별자"),
                        fieldWithPath("result.importer.name").description("수입사 이름"),

                        fieldWithPath("result.winery").description("와이너리 오브젝트"),
                        fieldWithPath("result.winery.uuid").description("와이너리 식별자"),
                        fieldWithPath("result.winery.nameEnglish").description("와이너리 이름(영어)"),
                        fieldWithPath("result.winery.nameKorean").description("와이너리 이름(한글)"),

                        fieldWithPath("result.wineryRegion").description("와이너리 지역 오브젝트"),
                        fieldWithPath("result.wineryRegion.uuid").description("와이너리 지역 식별자"),
                        fieldWithPath("result.wineryRegion.nameEnglish").description("와이너리 지역 이름(영어)"),
                        fieldWithPath("result.wineryRegion.nameKorean").description("와이너리 지역 이름(한글)"),

                        fieldWithPath("result.tags").description("태그 오브젝트"),
                        fieldWithPath("result.tags.AROMA[]").description("와인의 향"),
                        fieldWithPath("result.tags.PAIRING[]").description("와인과 어울리는 음식"),

                        fieldWithPath("result.grapes[]").description("포도 오브젝트"),
                        fieldWithPath("result.grapes[].uuid").description("포도 식별자"),
                        fieldWithPath("result.grapes[].nameEnglish").description("포도 이름(영어)"),
                        fieldWithPath("result.grapes[].nameKorean").description("포도 이름(한글)")
                    )
                )
            )


    }

    @Transactional
    @Test
    @Order(8)
    @DisplayName("와인 생성, 성공")
    fun wineCreateSuccess() {
        val uri = Uris.WINE_ROOT

        val nameEnglish = "Dom Pérignon"
        val nameKorean = "돔 페리뇽"
        val type = "WHITE"
        val alcohol = 12.0F
        val acidity = 1
        val body = 5
        val sweetness = 2
        val tannin = 4
        val servingTemperature = 6.0F
        val score = 8.0F
        val price = 350000
        val style = "French Champagne"
        val grade = "AOC(AOP)"

        val importer: Importer = wineGateway.findImporterById(12)
        val importerUuid: String = importer.uuid!!

        val winery = wineryGateway.findById(20)
        val wineryUuid: String = winery.uuid!!

        val region: Region = regionGateway.findById(9)
        val regionUuid: String = region.uuid!!

        val request = WinePayloads
            .CreateRequest(
                nameEnglish,
                nameKorean,
                type,
                alcohol,
                acidity,
                body,
                sweetness,
                tannin,
                servingTemperature,
                score,
                price,
                style,
                grade,
                importerUuid,
                wineryUuid,
                regionUuid
            )

        val resultActions = mockMvc.perform(
            post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(stringUtils.toJson(request))
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message", notNullValue()))
            .andExpect(jsonPath("$.message", `is`(MessageCode.SUCCESS.name)))

        assertDoesNotThrow {
            wineRepository.findByNameEnglish(nameEnglish)
                .orElseThrow { CommonApplicationException(MessageCode.NOT_FOUND_WINE) }
        }

        resultActions
            .andDo(
                restDocs.document(
                    requestFields(
                        fieldWithPath("nameEnglish").description("와인 이름(영어)"),
                        fieldWithPath("nameKorean").description("와인 이름(한글)"),
                        fieldWithPath("type").description("와인의 종류"),
                        fieldWithPath("alcohol").description("와인의 도수"),
                        fieldWithPath("acidity").description("산도"),
                        fieldWithPath("body").description("바디감"),
                        fieldWithPath("sweetness").description("당도"),
                        fieldWithPath("tannin").description("타닌"),
                        fieldWithPath("servingTemperature").description("권장 섭취온도"),
                        fieldWithPath("score").description("와인의 점수"),
                        fieldWithPath("price").description("와인의 가격"),
                        fieldWithPath("style").description("와인의 스타일"),
                        fieldWithPath("grade").description("와인의 등급"),
                        fieldWithPath("importerUuid").description("수입사 식별자"),
                        fieldWithPath("wineryUuid").description("와이너리 식별자"),
                        fieldWithPath("regionUuid").description("지역 식별자")
                    ),
                    responseFields(
                        fieldWithPath("message").description("시스템 메시지")
                    )
                )
            )

    }

    @Transactional
    @Test
    @Order(9)
    @DisplayName("와인 수정, 성공")
    fun wineUpdateSuccess() {
        val uri = Uris.WINE_ROOT

        val wine = wineGateway.findById(34)
        val uuid = wine.uuid!!

        val nameEnglish = "Dom Pérignon"
        val nameKorean = "돔 페리뇽"
        val type = "WHITE"
        val alcohol = 12.0F
        val acidity = 1
        val body = 5
        val sweetness = 2
        val tannin = 4
        val servingTemperature = 6.0F
        val score = 8.0F
        val price = 350000
        val style = "French Champagne"
        val grade = "AOC(AOP)"

        val importer: Importer = wineGateway.findImporterById(12)
        val importerUuid: String = importer.uuid!!

        val winery = wineryGateway.findById(22)
        val wineryUuid: String = winery.uuid!!

        val region: Region = regionGateway.findById(9)
        val regionUuid: String = region.uuid!!

        val request = WinePayloads
            .UpdateRequest(
                uuid,
                nameEnglish,
                nameKorean,
                type,
                alcohol,
                acidity,
                body,
                sweetness,
                tannin,
                servingTemperature,
                score,
                price,
                style,
                grade,
                importerUuid,
                wineryUuid,
                regionUuid
            )

        val resultActions = mockMvc.perform(
            patch(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(stringUtils.toJson(request))
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message", notNullValue()))
            .andExpect(jsonPath("$.message", `is`(MessageCode.SUCCESS.name)))

        val updatedWine = wineGateway.findByUuid(uuid)

        assertEquals(updatedWine.uuid, uuid)
        assertEquals(updatedWine.nameEnglish, nameEnglish)
        assertEquals(updatedWine.nameKorean, nameKorean)
        assertEquals(updatedWine.type, type)
        assertEquals(updatedWine.alcohol, alcohol)
        assertEquals(updatedWine.acidity, acidity)
        assertEquals(updatedWine.body, body)
        assertEquals(updatedWine.sweetness, sweetness)
        assertEquals(updatedWine.tannin, tannin)
        assertEquals(updatedWine.servingTemperature, servingTemperature)
        assertEquals(updatedWine.score, score)
        assertEquals(updatedWine.price, price)
        assertEquals(updatedWine.style, style)
        assertEquals(updatedWine.grade, grade)

        resultActions
            .andDo(
                restDocs.document(
                    requestFields(
                        fieldWithPath("uuid").description("와인 식별자"),
                        fieldWithPath("nameEnglish").description("와인 이름(영어)").optional(),
                        fieldWithPath("nameKorean").description("와인 이름(한글)").optional(),
                        fieldWithPath("type").description("와인의 종류").optional(),
                        fieldWithPath("alcohol").description("와인의 도수").optional(),
                        fieldWithPath("acidity").description("산도").optional(),
                        fieldWithPath("body").description("바디감").optional(),
                        fieldWithPath("sweetness").description("당도").optional(),
                        fieldWithPath("tannin").description("타닌").optional(),
                        fieldWithPath("servingTemperature").description("권장 섭취온도").optional(),
                        fieldWithPath("score").description("와인의 점수").optional(),
                        fieldWithPath("price").description("와인의 가격").optional(),
                        fieldWithPath("style").description("와인의 스타일").optional(),
                        fieldWithPath("grade").description("와인의 등급").optional(),
                        fieldWithPath("importerUuid").description("수입사 식별자").optional(),
                        fieldWithPath("wineryUuid").description("와이너리 식별자").optional(),
                        fieldWithPath("regionUuid").description("지역 식별자").optional()
                    ),
                    responseFields(
                        fieldWithPath("message").description("시스템 메시지")
                    )
                )
            )

    }

    @Transactional
    @Test
    @Order(10)
    @DisplayName("와인 삭제, 성공")
    fun wineDeleteSuccess() {
        val uri = Uris.WINE_ROOT + Uris.REST_NAME_UUID

        val wine = wineGateway.findById(34)
        val uuid = wine.uuid!!

        val resultActions = mockMvc.perform(
            delete(uri, uuid)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message", notNullValue()))
            .andExpect(jsonPath("$.message", `is`(MessageCode.SUCCESS.name)))

        assertThrows(CommonApplicationException::class.java) { wineGateway.findByUuid(uuid) }

        resultActions.andDo(
            restDocs.document(
                pathParameters(
                    parameterWithName("uuid").description("와인 식별자")
                ),
                responseFields(
                    fieldWithPath("message").description("시스템 메시지")
                )
            )
        )

    }
}
