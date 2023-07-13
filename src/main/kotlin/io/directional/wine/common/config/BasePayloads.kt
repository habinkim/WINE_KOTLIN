package io.directional.wine.common.config

class BasePayloads {

    open class SimpleResponse(
        var uuid: String? = null,
        var nameEnglish: String? = null,
        var nameKorean: String? = null
    )

}
