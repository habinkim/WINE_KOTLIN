package io.directional.wine.winery.controller

import io.directional.wine.base.ControllerBaseTest
import io.directional.wine.common.config.Uris
import io.directional.wine.common.response.MessageCode
import io.directional.wine.exception.CommonApplicationException
import io.directional.wine.payload.winery.WineryPayloads
import io.directional.wine.region.service.RegionGateway
import io.directional.wine.winery.repository.WineryRepository
import io.directional.wine.winery.service.WineryGateway
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

class WineryControllerTest : ControllerBaseTest() {

    @Autowired
    private lateinit var wineryRepository: WineryRepository

    @Autowired
    private lateinit var wineryGateway: WineryGateway

    @Autowired
    private lateinit var regionGateway: RegionGateway

    @Transactional
    @Test
    @Order(1)
    @DisplayName("와이너리 리스트 조회, 성공")
    fun wineryListSuccess() {
        val uri = Uris.WINERY_ROOT + "/list"

        val request = WineryPayloads.ListRequest.invoke(0, 10, null, null, null)
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

            .andExpect(jsonPath("$.message", `is`(MessageCode.SUCCESS.name)))
            .andExpect(
                jsonPath(
                    "$.result.content[0].uuid",
                    matchesPattern("([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})")
                )
            )
            .andExpect(jsonPath("$.result.content[0].nameEnglish", `is`("1881 Napa")))
            .andExpect(jsonPath("$.result.content[0].nameKorean", `is`("1881 나파")))

            .andExpect(jsonPath("$.result.pageable.offset", `is`(0)))
            .andExpect(jsonPath("$.result.pageable.pageNumber", `is`(0)))
            .andExpect(jsonPath("$.result.pageable.pageSize", `is`(10)))
            .andExpect(jsonPath("$.result.pageable.paged", `is`(true)))
            .andExpect(jsonPath("$.result.pageable.unpaged", `is`(false)))

            .andExpect(jsonPath("$.result.pageable.sort.empty", `is`(false)))
            .andExpect(jsonPath("$.result.pageable.sort.unsorted", `is`(false)))
            .andExpect(jsonPath("$.result.pageable.sort.sorted", `is`(true)))

            .andExpect(jsonPath("$.result.last", `is`(false)))
            .andExpect(jsonPath("$.result.totalPages", `is`(23)))
            .andExpect(jsonPath("$.result.totalElements", `is`(223)))
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
                        fieldWithPath("pageNo").description("페이지 번호").optional(),
                        fieldWithPath("pageSize").description("페이지 당 크기").optional(),
                        fieldWithPath("regionUuid").description("지역 고유번호").optional(),
                        fieldWithPath("nameEnglish").description("와이너리 이름(영어)").optional(),
                        fieldWithPath("nameKorean").description("와이너리 이름(한글)").optional()
                    ),
                    responseFields(
                        fieldWithPath("message").description("시스템 메시지"),
                        fieldWithPath("result.content[]").description("오브젝트"),
                        fieldWithPath("result.content[].uuid").description("고유번호"),
                        fieldWithPath("result.content[].nameEnglish").description("와이너리 이름(영어)"),
                        fieldWithPath("result.content[].nameKorean").description("와이너리 이름(한글)"),

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
    @DisplayName("와이너리 상세 조회, 성공")
    fun wineryDetailSuccess() {
        val uri = Uris.WINERY_ROOT + Uris.REST_NAME_UUID

        val winery = wineryGateway.findById(20)

        mockMvc.perform(
            get(uri, winery.uuid)
        )
            .andExpect(jsonPath("$.message", notNullValue()))
            .andExpect(jsonPath("$.result", notNullValue()))
            .andExpect(jsonPath("$.result.uuid", notNullValue()))
            .andExpect(jsonPath("$.result.nameEnglish", notNullValue()))
            .andExpect(jsonPath("$.result.nameKorean", notNullValue()))

            .andExpect(jsonPath("$.result.wine[0].uuid", notNullValue()))
            .andExpect(jsonPath("$.result.wine[0].nameEnglish", notNullValue()))
            .andExpect(jsonPath("$.result.wine[0].nameKorean", notNullValue()))

            .andExpect(jsonPath("$.message", `is`(MessageCode.SUCCESS.name)))
            .andExpect(jsonPath("$.result.uuid", `is`(winery.uuid)))
            .andExpect(jsonPath("$.result.nameEnglish", `is`(winery.nameEnglish)))
            .andExpect(jsonPath("$.result.nameKorean", `is`(winery.nameKorean)))

            .andExpect(
                jsonPath(
                    "$.result.wine[0].uuid",
                    matchesPattern("([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})")
                )
            )
            .andExpect(jsonPath("$.result.wine[0].nameEnglish", `is`("Bodegas Neleman, Just Good Wine White")))
            .andExpect(jsonPath("$.result.wine[0].nameKorean", `is`("보데가스 넬레만, 저스트 화이트")))

            .andDo(
                restDocs.document(
                    pathParameters(
                        parameterWithName("uuid").description("와이너리 식별자")
                    ),
                    responseFields(
                        fieldWithPath("message").description("시스템 메시지"),
                        fieldWithPath("result").description("오브젝트"),
                        fieldWithPath("result.uuid").description("와이너리 식별자"),
                        fieldWithPath("result.nameEnglish").description("와이너리 이름(영어)"),
                        fieldWithPath("result.nameKorean").description("와이너리 이름(한글)"),

                        fieldWithPath("result.wine[]").description("와인").optional(),
                        fieldWithPath("result.wine[].uuid").description("와인 식별자"),
                        fieldWithPath("result.wine[].nameEnglish").description("와인 이름(영어)"),
                        fieldWithPath("result.wine[].nameKorean").description("와인 이름(한글)"),
                    )
                )
            )
    }

    @Transactional
    @Test
    @Order(3)
    @DisplayName("와이너리 생성, 성공")
    fun wineryCreateSuccess() {

        val uri = Uris.WINERY_ROOT
        val region = regionGateway.findById(11)

        val nameEnglish = "haaabin Salon"
        val request = WineryPayloads.CreateRequest(nameEnglish, "하빈 살롱", region.uuid!!)

        val resultActions = mockMvc.perform(
            post(uri)
                .content(stringUtils.toJson(request))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message", notNullValue()))
            .andExpect(jsonPath("$.message", `is`(MessageCode.SUCCESS.name)))

        assertDoesNotThrow {
            wineryRepository.findByNameEnglish(nameEnglish)
                .orElseThrow { CommonApplicationException(MessageCode.NOT_FOUND_GRAPE) }
        }

        resultActions
            .andDo(
                restDocs.document(
                    requestFields(
                        fieldWithPath("nameEnglish").description("와이너리 이름 (영어)"),
                        fieldWithPath("nameKorean").description("와이너리 이름 (한글)"),
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
    @Order(4)
    @DisplayName("와이너리 수정, 성공")
    fun wineryUpdateSuccess() {

        val uri = Uris.WINERY_ROOT
        val winery = wineryGateway.findById(11)
        val region = regionGateway.findById(11)

        val nameEnglish = "haaabin Salon"
        val nameKorean = "하빈 살롱"
        val uuid = winery.uuid!!
        val regionUuid = region.uuid
        val request = WineryPayloads.UpdateRequest(uuid, nameEnglish, nameKorean, regionUuid)

        val resultActions = mockMvc.perform(
            patch(uri)
                .content(stringUtils.toJson(request))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message", notNullValue()))
            .andExpect(jsonPath("$.message", `is`(MessageCode.SUCCESS.name)))

        val updatedWinery = wineryGateway.findByUuid(uuid)
        assertEquals(updatedWinery.uuid, uuid)
        assertEquals(updatedWinery.nameEnglish, nameEnglish)
        assertEquals(updatedWinery.nameKorean, nameKorean)
        assertEquals(updatedWinery.region!!.uuid, regionUuid)

        resultActions
            .andDo(
                restDocs.document(
                    requestFields(
                        fieldWithPath("uuid").description("와이너리 식별자"),
                        fieldWithPath("nameEnglish").description("와이너리 이름(영어)"),
                        fieldWithPath("nameKorean").description("와이너리 이름(한글)"),
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
    @Order(5)
    @DisplayName("와이너리 삭제, 성공")
    fun wineryDeleteSuccess() {

        val uri = Uris.WINERY_ROOT + Uris.REST_NAME_UUID
        val winery = wineryGateway.findById(11)
        val uuid = winery.uuid!!

        val resultActions = mockMvc.perform(
            delete(uri, uuid)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message", notNullValue()))
            .andExpect(jsonPath("$.message", `is`(MessageCode.SUCCESS.name)))

        assertThrows(CommonApplicationException::class.java) { wineryGateway.findByUuid(uuid) }

        resultActions
            .andDo(
                restDocs.document(
                    pathParameters(
                        parameterWithName("uuid").description("와이너리 고유번호")
                    ),
                    responseFields(
                        fieldWithPath("message").description("시스템 메시지")
                    )
                )
            )

    }
}
