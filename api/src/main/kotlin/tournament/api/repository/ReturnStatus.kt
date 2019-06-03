package tournament.api.repository

import io.micronaut.http.HttpStatus

data class ReturnStatus(
    val message: String,
    val httpStatus: HttpStatus
)