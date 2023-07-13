package io.directional.wine.exception

import io.directional.wine.common.response.MessageCode


class CommonApplicationException(val messageCode: MessageCode? = null) : RuntimeException()
