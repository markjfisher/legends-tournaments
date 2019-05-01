package tournament.api

import io.micronaut.http.HttpStatus
import tournament.api.repository.ReturnStatus

fun <T> createPair(item: T, httpStatus: HttpStatus = HttpStatus.OK, message: String = ""): Pair<ReturnStatus, T> {
    return Pair(ReturnStatus(message = message, httpStatus = httpStatus), item)
}
