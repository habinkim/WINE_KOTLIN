package io.directional.wine.common.response

import org.springframework.http.HttpStatus
import java.util.*

enum class MessageCode(
    val httpStatus: HttpStatus,
    val code: String
) {

    SUCCESS(HttpStatus.OK, "0000"),
    CREATED(HttpStatus.CREATED, "0001"),
    NOT_FOUND_DATA(HttpStatus.BAD_REQUEST, "9998"),
    ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "9999"),
    EXCEPTION_ILLEGAL_ARGUMENT(HttpStatus.BAD_REQUEST, "9100"),

    NOT_FOUND_REGION(HttpStatus.BAD_REQUEST, "1100"),
    NOT_FOUND_GRAPE(HttpStatus.BAD_REQUEST, "1200"),
    NOT_FOUND_GRAPE_SHARE(HttpStatus.BAD_REQUEST, "1201"),
    NOT_FOUND_WINE(HttpStatus.BAD_REQUEST, "1300"),
    NOT_FOUND_IMPORTER(HttpStatus.BAD_REQUEST, "1301"),
    NOT_FOUND_WINERY(HttpStatus.BAD_REQUEST, "1400");

    companion object {
        operator fun get(name: String): Optional<MessageCode> {
            return Arrays.stream(values())
                .filter { env: MessageCode -> env.name == name }
                .findFirst()
        }
    }
}
