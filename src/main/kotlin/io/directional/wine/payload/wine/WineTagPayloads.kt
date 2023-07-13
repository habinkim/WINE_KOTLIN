package io.directional.wine.payload.wine

import io.directional.wine.common.enums.WineTagType

class WineTagPayloads {
    class SimpleResponse (
        val uuid: String? = null,
        val type: WineTagType? = null,
        val value: String? = null,
    )

}
