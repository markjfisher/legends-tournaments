package tournament.api

import tournament.api.repository.ServiceResult
import tournament.api.repository.ServiceStatus
import tournament.api.repository.Tournament

fun createResult(item: Tournament, httpStatus: ServiceStatus = ServiceStatus.OK, message: String = ""): ServiceResult {
    return ServiceResult(message = message, serviceStatus = httpStatus, tournament = item)
}
