package io.directional.wine.region.controller

import io.directional.wine.base.ControllerBaseTest
import io.directional.wine.common.config.Uris
import io.directional.wine.common.response.MessageCode
import io.directional.wine.domain.region.Region
import io.directional.wine.exception.CommonApplicationException
import io.directional.wine.payload.region.RegionPayloads
import io.directional.wine.region.repository.RegionRepository
import io.directional.wine.region.service.RegionGateway
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
import org.springframework.restdocs.request.RequestDocumentation.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

class RegionControllerTest : ControllerBaseTest() {

    @Autowired
    private lateinit var regionGateway: RegionGateway

    @Autowired
    private lateinit var regionRepository: RegionRepository

    @Transactional
    @Test
    @Order(1)
    @DisplayName("지역 리스트 조회, 성공")
    fun regionListSuccess() {
        val uri = Uris.REGION_ROOT + "/list"

        val request = RegionPayloads.ListRequest.invoke(0, 5, null, null, null)

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

            .andExpect(
                jsonPath(
                    "$.result.content[0].uuid",
                    matchesPattern("([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})")
                )
            )

            .andExpect(jsonPath("$.result.pageable.offset", `is`(0)))
            .andExpect(jsonPath("$.result.pageable.pageNumber", `is`(0)))
            .andExpect(jsonPath("$.result.pageable.pageSize", `is`(5)))
            .andExpect(jsonPath("$.result.pageable.paged", `is`(true)))
            .andExpect(jsonPath("$.result.pageable.unpaged", `is`(false)))

            .andExpect(jsonPath("$.result.pageable.sort.empty", `is`(false)))
            .andExpect(jsonPath("$.result.pageable.sort.unsorted", `is`(false)))
            .andExpect(jsonPath("$.result.pageable.sort.sorted", `is`(true)))

            .andExpect(jsonPath("$.result.last", `is`(false)))
            .andExpect(jsonPath("$.result.totalPages", `is`(30)))
            .andExpect(jsonPath("$.result.totalElements", `is`(149)))
            .andExpect(jsonPath("$.result.first", `is`(true)))
            .andExpect(jsonPath("$.result.size", `is`(5)))
            .andExpect(jsonPath("$.result.number", `is`(0)))
            .andExpect(jsonPath("$.result.sort.empty", `is`(false)))
            .andExpect(jsonPath("$.result.sort.unsorted", `is`(false)))
            .andExpect(jsonPath("$.result.sort.sorted", `is`(true)))
            .andExpect(jsonPath("$.result.numberOfElements", `is`(5)))
            .andExpect(jsonPath("$.result.empty", `is`(false)))

            .andDo(
                restDocs.document(
                    requestFields(
                        fieldWithPath("parentUuid").description("상위 지역 고유번호").optional(),
                        fieldWithPath("nameEnglish").description("지역 이름(영어)").optional(),
                        fieldWithPath("nameKorean").description("지역 이름(한글)").optional(),
                        fieldWithPath("pageNo").description("페이지 번호").optional(),
                        fieldWithPath("pageSize").description("페이지 당 크기").optional()
                    ),
                    responseFields(
                        fieldWithPath("message").description("시스템 메시지"),
                        fieldWithPath("result.content[]").description("오브젝트"),
                        fieldWithPath("result.content[].uuid").description("고유번호"),
                        fieldWithPath("result.content[].nameEnglish").description("지역 이름(영어)"),
                        fieldWithPath("result.content[].nameKorean").description("지역 이름(한글)"),

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
    @DisplayName("지역 상세 조회 CASE 1 (포도, 와이너리, 와인), 성공")
    fun regionDetailCase1Success() {

        val uri = Uris.REGION_ROOT + Uris.REST_NAME_UUID

        val id: Long = 9
        val region: Region = regionGateway.findById(id)

        mockMvc.perform(
            get(uri, region.uuid)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message", notNullValue()))
            .andExpect(jsonPath("$.result", notNullValue()))

            .andExpect(jsonPath("$.result.uuid", notNullValue()))
            .andExpect(jsonPath("$.result.nameEnglish", notNullValue()))
            .andExpect(jsonPath("$.result.nameKorean", notNullValue()))

            .andExpect(jsonPath("$.result.parent").isEmpty)

            .andExpect(jsonPath("$.result.grape[0]", notNullValue()))
            .andExpect(jsonPath("$.result.grape[0].uuid", notNullValue()))
            .andExpect(jsonPath("$.result.grape[0].nameEnglish", notNullValue()))
            .andExpect(jsonPath("$.result.grape[0].nameKorean", notNullValue()))

            .andExpect(jsonPath("$.result.winery[0]", notNullValue()))
            .andExpect(jsonPath("$.result.winery[0].uuid", notNullValue()))
            .andExpect(jsonPath("$.result.winery[0].nameEnglish", notNullValue()))
            .andExpect(jsonPath("$.result.winery[0].nameKorean", notNullValue()))

            .andExpect(jsonPath("$.result.wine[0]", notNullValue()))
            .andExpect(jsonPath("$.result.wine[0].uuid", notNullValue()))
            .andExpect(jsonPath("$.result.wine[0].nameEnglish", notNullValue()))
            .andExpect(jsonPath("$.result.wine[0].nameKorean", notNullValue()))

            .andExpect(jsonPath("$.message", `is`(MessageCode.SUCCESS.name)))
            .andExpect(jsonPath("$.result.uuid", `is`(region.uuid)))
            .andExpect(jsonPath("$.result.nameEnglish", `is`(region.nameEnglish)))
            .andExpect(jsonPath("$.result.nameKorean", `is`(region.nameKorean)))

            .andExpect(jsonPath("$.result.grape[0].uuid", matchesPattern("([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})")))
            .andExpect(jsonPath("$.result.grape[0].nameEnglish", `is`("Albarino(Alvarinho)")))
            .andExpect(jsonPath("$.result.grape[0].nameKorean", `is`("알바리뇨")))

            .andExpect(jsonPath("$.result.winery[0].uuid", matchesPattern("([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})")))
            .andExpect(jsonPath("$.result.winery[0].nameEnglish", `is`("Azores Wine Company")))
            .andExpect(jsonPath("$.result.winery[0].nameKorean", `is`("아소르스 와인 컴퍼니")))

            .andExpect(jsonPath("$.result.wine[0].uuid", matchesPattern("([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})")))
            .andExpect(jsonPath("$.result.wine[0].nameEnglish", `is`("Aluado Chardonnay")))
            .andExpect(jsonPath("$.result.wine[0].nameKorean", `is`("알루아도 샤도네이")))
            .andDo(
                restDocs.document(
                    pathParameters(
                        parameterWithName("uuid").description("지역 식별자")
                    ),
                    responseFields(
                        fieldWithPath("message").description("시스템 메시지"),
                        fieldWithPath("result").description("오브젝트"),
                        fieldWithPath("result.uuid").description("오브젝트"),
                        fieldWithPath("result.nameEnglish").description("지역 이름(영어)"),
                        fieldWithPath("result.nameKorean").description("지역 이름(한글)"),

                        fieldWithPath("result.parent[]").type(JsonFieldType.ARRAY).description("상위 지역"),

                        fieldWithPath("result.grape[]").description("포도"),
                        fieldWithPath("result.grape[].uuid").description("포도 식별자"),
                        fieldWithPath("result.grape[].nameEnglish").description("포도 이름(영어)"),
                        fieldWithPath("result.grape[].nameKorean").description("포도 이름(한글)"),

                        fieldWithPath("result.winery[]").description("와이너리"),
                        fieldWithPath("result.winery[].uuid").description("와이너리 식별자"),
                        fieldWithPath("result.winery[].nameEnglish").description("와이너리 이름(영어)"),
                        fieldWithPath("result.winery[].nameKorean").description("와이너리 이름(한글)"),

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
    @DisplayName("지역 상세 조회 CASE 2 (상위 지역, 와인), 성공")
    fun regionDetailCase2Success() {

        val uri = Uris.REGION_ROOT + Uris.REST_NAME_UUID

        val id: Long = 107
        val region: Region = regionGateway.findById(id)

        mockMvc.perform(
            get(uri, region.uuid)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message", notNullValue()))
            .andExpect(jsonPath("$.result", notNullValue()))

            .andExpect(jsonPath("$.result.uuid", notNullValue()))
            .andExpect(jsonPath("$.result.nameEnglish", notNullValue()))
            .andExpect(jsonPath("$.result.nameKorean", notNullValue()))

            .andExpect(jsonPath("$.result.parent[0]", notNullValue()))
            .andExpect(jsonPath("$.result.parent[0].uuid", notNullValue()))
            .andExpect(jsonPath("$.result.parent[0].nameEnglish", notNullValue()))
            .andExpect(jsonPath("$.result.parent[0].nameKorean", notNullValue()))

            .andExpect(jsonPath("$.result.grape").isEmpty)

            .andExpect(jsonPath("$.result.winery").isEmpty)

            .andExpect(jsonPath("$.result.wine[0]", notNullValue()))
            .andExpect(jsonPath("$.result.wine[0].uuid", notNullValue()))
            .andExpect(jsonPath("$.result.wine[0].nameEnglish", notNullValue()))
            .andExpect(jsonPath("$.result.wine[0].nameKorean", notNullValue()))

            .andExpect(jsonPath("$.message", `is`(MessageCode.SUCCESS.name)))
            .andExpect(jsonPath("$.result.uuid", `is`(region.uuid)))
            .andExpect(jsonPath("$.result.nameEnglish", `is`(region.nameEnglish)))
            .andExpect(jsonPath("$.result.nameKorean", `is`(region.nameKorean)))

            .andExpect(jsonPath("$.result.parent[0].uuid", matchesPattern("([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})")))
            .andExpect(jsonPath("$.result.parent[0].nameEnglish", `is`("Victoria")))
            .andExpect(jsonPath("$.result.parent[0].nameKorean", `is`("빅토리아")))

            .andExpect(jsonPath("$.result.wine[0].uuid", matchesPattern("([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})")))
            .andExpect(jsonPath("$.result.wine[0].nameEnglish", `is`("Bass Phillip, Crown Prince Pinot Noir")))
            .andExpect(jsonPath("$.result.wine[0].nameKorean", `is`("바스 필립, 크라운 프린스 피노누아")))
            .andDo(
                restDocs.document(
                    pathParameters(
                        parameterWithName("uuid").description("지역 식별자")
                    ),
                    responseFields(
                        fieldWithPath("message").description("시스템 메시지"),
                        fieldWithPath("result").description("오브젝트"),
                        fieldWithPath("result.uuid").description("오브젝트"),
                        fieldWithPath("result.nameEnglish").description("지역 이름(영어)"),
                        fieldWithPath("result.nameKorean").description("지역 이름(한글)"),

                        fieldWithPath("result.parent[]").description("상위 지역"),
                        fieldWithPath("result.parent[].uuid").description("상위 지역 식별자"),
                        fieldWithPath("result.parent[].nameEnglish").description("상위 지역 이름(영어)"),
                        fieldWithPath("result.parent[].nameKorean").description("상위 지역 이름(한글)"),

                        fieldWithPath("result.grape[]").type(JsonFieldType.ARRAY).description("포도"),

                        fieldWithPath("result.winery[]").type(JsonFieldType.ARRAY).description("와이너리"),

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
    @Order(4)
    @DisplayName("지역 생성, 성공")
    fun regionCreateSuccess() {
        val uri = Uris.REGION_ROOT

        val nameEnglish = "South Korea"
        val nameKorean = "대한민국"
        val request = RegionPayloads.CreateRequest(nameEnglish, nameKorean)

        val resultActions = mockMvc.perform(
            post(uri)
                .content(stringUtils.toJson(request))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message", notNullValue()))
            .andExpect(jsonPath("$.message", `is`(MessageCode.SUCCESS.name)))

        assertDoesNotThrow {
            regionRepository.findByNameEnglish(nameEnglish)
                .orElseThrow { CommonApplicationException(MessageCode.NOT_FOUND_REGION) }
        }

        resultActions
            .andDo(
                restDocs.document(
                    requestFields(
                        fieldWithPath("nameEnglish").description("지역 이름(영어)"),
                        fieldWithPath("nameKorean").description("지역 이름(한글)"),
                        fieldWithPath("parentUuid").description("상위 지역 식별자").optional()
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
    @DisplayName("지역 수정, 성공")
    fun regionUpdateSuccess() {

        val uri = Uris.REGION_ROOT

        val id: Long = 9
        val region: Region = regionGateway.findById(id)

        val nameEnglish = "South Korea"
        val nameKorean = "대한민국"
        val request = RegionPayloads.UpdateRequest(region.uuid!!, nameEnglish, nameKorean)

        val resultActions = mockMvc.perform(
            patch(uri)
                .content(stringUtils.toJson(request))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message", notNullValue()))
            .andExpect(jsonPath("$.message", `is`(MessageCode.SUCCESS.name)))

        val updatedRegion = regionGateway.findByUuid(region.uuid!!)
        assertEquals(updatedRegion.nameEnglish, nameEnglish)
        assertEquals(updatedRegion.nameKorean, nameKorean)

        resultActions
            .andDo(
                restDocs.document(
                    requestFields(
                        fieldWithPath("uuid").description("지역 고유번호"),
                        fieldWithPath("nameEnglish").description("지역 이름(영어)").optional(),
                        fieldWithPath("nameKorean").description("지역 이름(한글)").optional(),
                        fieldWithPath("parentUuid").description("상위 지역 식별자").type(JsonFieldType.STRING).optional()
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
    @DisplayName("지역 삭제, 성공")
    fun regionDeleteSuccess() {

        val uri = Uris.REGION_ROOT + Uris.REST_NAME_UUID

        val id: Long = 149
        val region: Region = regionGateway.findById(id)
        val uuid = region.uuid

        val resultActions = mockMvc.perform(
            delete(uri, uuid)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message", notNullValue()))
            .andExpect(jsonPath("$.message", `is`(MessageCode.SUCCESS.name)))

        assertThrows(CommonApplicationException::class.java) { regionGateway.findByUuid(uuid!!) }

        resultActions
            .andDo(
                restDocs.document(
                    pathParameters(
                        parameterWithName("uuid").description("지역 고유번호")
                    ),
                    responseFields(
                        fieldWithPath("message").description("시스템 메시지")
                    )
                )
            )
    }

}
