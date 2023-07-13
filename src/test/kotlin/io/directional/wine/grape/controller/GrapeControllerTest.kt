package io.directional.wine.grape.controller

import io.directional.wine.base.ControllerBaseTest
import io.directional.wine.common.config.Uris
import io.directional.wine.common.response.MessageCode
import io.directional.wine.exception.CommonApplicationException
import io.directional.wine.grape.repository.GrapeRepository
import io.directional.wine.grape.service.GrapeGateway
import io.directional.wine.grape.service.GrapeService
import io.directional.wine.payload.grape.GrapePayloads
import io.directional.wine.region.service.RegionGateway
import io.directional.wine.region.service.RegionService
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

class GrapeControllerTest : ControllerBaseTest() {

    @Autowired
    private lateinit var grapeGateway: GrapeGateway

    @Autowired
    private lateinit var regionGateway: RegionGateway

    @Autowired
    private lateinit var grapeRepository: GrapeRepository

    @Transactional
    @Test
    @Order(1)
    @DisplayName("포도 리스트 조회, 성공")
    fun grapeListSuccess() {

        val uri = Uris.GRAPE_ROOT + "/list"
        val request = GrapePayloads.ListRequest.invoke(
            null, null, "body",
            null, null, null, null
        )

        mockMvc.perform(
            post(uri)
                .content(stringUtils.toJson(request))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message", notNullValue()))
            .andExpect(jsonPath("$.result", notNullValue()))
            .andExpect(jsonPath("$.result.content[0]", notNullValue()))

            .andExpect(jsonPath("$.result.content[0].uuid", notNullValue()))
            .andExpect(jsonPath("$.result.content[0].nameEnglish", notNullValue()))
            .andExpect(jsonPath("$.result.content[0].nameKorean", notNullValue()))

            .andExpect(jsonPath("$.result.content[0].region[0].uuid", notNullValue()))
            .andExpect(jsonPath("$.result.content[0].region[0].nameEnglish", notNullValue()))
            .andExpect(jsonPath("$.result.content[0].region[0].nameKorean", notNullValue()))

            .andExpect(jsonPath("$.message", `is`(MessageCode.SUCCESS.name)))
            .andExpect(
                jsonPath(
                    "$.result.content[0].uuid",
                    matchesPattern("([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})")
                )
            )
            .andExpect(jsonPath("$.result.content[0].nameEnglish", `is`("Arinto")))
            .andExpect(jsonPath("$.result.content[0].nameKorean", `is`("아린토")))

            .andExpect(
                jsonPath(
                    "$.result.content[0].region[0].uuid",
                    matchesPattern("([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})")
                )
            )
            .andExpect(jsonPath("$.result.content[0].region[0].nameEnglish", `is`("Portugal")))
            .andExpect(jsonPath("$.result.content[0].region[0].nameKorean", `is`("포르투갈")))

            .andExpect(jsonPath("$.result.pageable.offset", `is`(0)))
            .andExpect(jsonPath("$.result.pageable.pageNumber", `is`(0)))
            .andExpect(jsonPath("$.result.pageable.pageSize", `is`(10)))
            .andExpect(jsonPath("$.result.pageable.paged", `is`(true)))
            .andExpect(jsonPath("$.result.pageable.unpaged", `is`(false)))

            .andExpect(jsonPath("$.result.pageable.sort.empty", `is`(false)))
            .andExpect(jsonPath("$.result.pageable.sort.unsorted", `is`(false)))
            .andExpect(jsonPath("$.result.pageable.sort.sorted", `is`(true)))

            .andExpect(jsonPath("$.result.last", `is`(false)))
            .andExpect(jsonPath("$.result.totalPages", `is`(9)))
            .andExpect(jsonPath("$.result.totalElements", `is`(88)))
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
                            .description("정렬 기준 필드 [nameKorean, acidity, body, sweetness, tannin]")
                            .optional(),
                        fieldWithPath("direction").type(JsonFieldType.STRING).description("정렬 방향 [ASC, DESC]")
                            .optional(),
                        fieldWithPath("regionUuid").type(JsonFieldType.STRING).description("지역 식별자").optional(),
                        fieldWithPath("nameEnglish").description("포도 이름(영어)").optional(),
                        fieldWithPath("nameKorean").description("포도 이름(한글)").optional()
                    ),
                    responseFields(
                        fieldWithPath("message").description("시스템 메시지"),
                        fieldWithPath("result.content[]").description("오브젝트"),
                        fieldWithPath("result.content[].uuid").description("포도 고유번호"),
                        fieldWithPath("result.content[].nameEnglish").description("포도 이름(영어)"),
                        fieldWithPath("result.content[].nameKorean").description("포도 이름(한글)"),

                        fieldWithPath("result.content[].region[]").description("포도 생산 지역").optional(),
                        fieldWithPath("result.content[].region[].uuid").description("지역 고유번호"),
                        fieldWithPath("result.content[].region[].nameEnglish").description("지역 이름(영어)"),
                        fieldWithPath("result.content[].region[].nameKorean").description("지역 이름(한글)"),


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
    @Order(2)
    @DisplayName("포도 상세 조회, 성공")
    fun grapeDetailSuccess() {

        val uri = Uris.GRAPE_ROOT + Uris.REST_NAME_UUID
        val grape = grapeGateway.findById(30)

        mockMvc.perform(
            get(uri, grape.uuid)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message", notNullValue()))
            .andExpect(jsonPath("$.result", notNullValue()))
            .andExpect(jsonPath("$.result.uuid", notNullValue()))
            .andExpect(jsonPath("$.result.nameEnglish", notNullValue()))
            .andExpect(jsonPath("$.result.nameKorean", notNullValue()))

            .andExpect(jsonPath("$.result.wine[0].uuid", notNullValue()))
            .andExpect(jsonPath("$.result.wine[0].nameEnglish", notNullValue()))
            .andExpect(jsonPath("$.result.wine[0].nameKorean", notNullValue()))

            .andExpect(jsonPath("$.result.region[0].uuid", notNullValue()))
            .andExpect(jsonPath("$.result.region[0].nameEnglish", notNullValue()))
            .andExpect(jsonPath("$.result.region[0].nameKorean", notNullValue()))

            .andExpect(jsonPath("$.message", `is`(MessageCode.SUCCESS.name)))
            .andExpect(jsonPath("$.result.uuid", `is`(grape.uuid)))
            .andExpect(jsonPath("$.result.nameEnglish", `is`(grape.nameEnglish)))
            .andExpect(jsonPath("$.result.nameKorean", `is`(grape.nameKorean)))

            .andExpect(jsonPath("$.result.wine[0].uuid", matchesPattern("([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})")))
            .andExpect(jsonPath("$.result.wine[0].nameEnglish", `is`("Bleasdale, The Wise One Tawny NV")))
            .andExpect(jsonPath("$.result.wine[0].nameKorean", `is`("브리스데일, 더 와이즈 원 타우니 NV")))

            .andExpect(jsonPath("$.result.region[0].uuid", matchesPattern("([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})")))
            .andExpect(jsonPath("$.result.region[0].nameEnglish", `is`("France")))
            .andExpect(jsonPath("$.result.region[0].nameKorean", `is`("프랑스")))

            .andDo(
                restDocs.document(
                    pathParameters(
                        parameterWithName("uuid").description("포도 식별자")
                    ),
                    responseFields(
                        fieldWithPath("message").description("시스템 메시지"),
                        fieldWithPath("result").description("오브젝트"),
                        fieldWithPath("result.uuid").description("포도 식별자"),
                        fieldWithPath("result.nameEnglish").description("포도 이름(영어)"),
                        fieldWithPath("result.nameKorean").description("포도 이름(한글)"),

                        fieldWithPath("result.wine[]").description("와인").optional(),
                        fieldWithPath("result.wine[].uuid").description("와인 식별자"),
                        fieldWithPath("result.wine[].nameEnglish").description("와인 이름(영어)"),
                        fieldWithPath("result.wine[].nameKorean").description("와인 이름(한글)"),

                        fieldWithPath("result.region[]").description("포도 생산 지역").optional(),
                        fieldWithPath("result.region[].uuid").description("지역 시겹랒"),
                        fieldWithPath("result.region[].nameEnglish").description("지역 이름(영어)"),
                        fieldWithPath("result.region[].nameKorean").description("지역 이름(한글)")
                    )
                )
            )

    }

    @Transactional
    @Test
    @Order(3)
    @DisplayName("포도 생성, 성공")
    fun grapeCreateSuccess() {

        val uri = Uris.GRAPE_ROOT
        val nameEnglish = "POODO"
        val request = GrapePayloads.CreateRequest(nameEnglish, "포오도", 3, 2, 5, 4)

        val resultActions = mockMvc.perform(
            post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(stringUtils.toJson(request))
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message", notNullValue()))
            .andExpect(jsonPath("$.message", `is`(MessageCode.SUCCESS.name)))

        assertDoesNotThrow {
            grapeRepository.findByNameEnglish(nameEnglish)
                .orElseThrow { CommonApplicationException(MessageCode.NOT_FOUND_GRAPE) }
        }

        resultActions
            .andDo(
                restDocs.document(
                    requestFields(
                        fieldWithPath("nameEnglish").description("포도 이름 (영어)"),
                        fieldWithPath("nameKorean").description("포도 이름 (영어)"),
                        fieldWithPath("acidity").description("산도"),
                        fieldWithPath("body").description("바디감"),
                        fieldWithPath("sweetness").description("당도"),
                        fieldWithPath("tannin").description("타닌"),

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
    @DisplayName("포도 수정, 성공")
    fun grapeUpdateSuccess() {

        val uri = Uris.GRAPE_ROOT

        val grape = grapeGateway.findById(30)
        val uuid = grape.uuid!!

        val nameEnglish = "POODO"
        val nameKorean = "포오도"
        val acidity = 3
        val body = 2
        val sweetness = 5
        val tannin = 4
        val request = GrapePayloads.UpdateRequest(uuid, nameEnglish, nameKorean, acidity, body, sweetness, tannin)

        val resultActions = mockMvc.perform(
            patch(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(stringUtils.toJson(request))
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message", notNullValue()))
            .andExpect(jsonPath("$.message", `is`(MessageCode.SUCCESS.name)))

        val updatedGrape = grapeGateway.findByUuid(uuid)
        assertEquals(updatedGrape.uuid, uuid)
        assertEquals(updatedGrape.nameEnglish, nameEnglish)
        assertEquals(updatedGrape.nameKorean, nameKorean)
        assertEquals(updatedGrape.acidity, acidity)
        assertEquals(updatedGrape.sweetness, sweetness)
        assertEquals(updatedGrape.tannin, tannin)

        resultActions
            .andDo(
                restDocs.document(
                    requestFields(
                        fieldWithPath("uuid").description("포도 식별자"),
                        fieldWithPath("nameEnglish").description("포도 이름 (영어)").optional(),
                        fieldWithPath("nameKorean").description("포도 이름 (영어)").optional(),
                        fieldWithPath("acidity").description("산도").optional(),
                        fieldWithPath("body").description("바디감").optional(),
                        fieldWithPath("sweetness").description("당도").optional(),
                        fieldWithPath("tannin").description("타닌").optional()
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
    @DisplayName("포도 삭제, 성공")
    fun grapeDeleteSuccess() {
        val uri = Uris.GRAPE_ROOT + Uris.REST_NAME_UUID

        val grape = grapeGateway.findById(30)
        val uuid = grape.uuid!!

        val resultActions = mockMvc.perform(
            delete(uri, uuid)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message", notNullValue()))
            .andExpect(jsonPath("$.message", `is`(MessageCode.SUCCESS.name)))

        assertThrows(CommonApplicationException::class.java) { grapeGateway.findByUuid(uuid) }

        resultActions
            .andDo(
                restDocs.document(
                    pathParameters(
                        parameterWithName("uuid").description("포도 고유번호")
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
    @DisplayName("포도 품종 비율 생성, 성공")
    fun grapeShareCreateSuccess() {
        val uri = Uris.GRAPE_SHARE_ROOT

        val grape = grapeGateway.findById(30)
        val region = regionGateway.findById(101)

        val grapeUuid = grape.uuid!!
        val regionUuid = region.uuid!!
        val request = GrapePayloads.CreateShareRequest(grapeUuid, regionUuid, 5.4F)

        val resultActions = mockMvc.perform(
            post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(stringUtils.toJson(request))
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message", notNullValue()))
            .andExpect(jsonPath("$.message", `is`(MessageCode.SUCCESS.name)))

        assertDoesNotThrow { grapeGateway.findGrapeShare(grapeUuid, regionUuid) }

        resultActions
            .andDo(
                restDocs.document(
                    requestFields(
                        fieldWithPath("grapeUuid").description("포도 고유번호"),
                        fieldWithPath("regionUuid").description("지역 고유번호"),
                        fieldWithPath("share").description("포도 품종 비율")
                    ),
                    responseFields(
                        fieldWithPath("message").description("시스템 메시지")
                    )
                )
            )
    }


}
