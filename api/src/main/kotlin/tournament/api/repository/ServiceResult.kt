package tournament.api.repository

data class ServiceResult(
    val serviceStatus: ServiceStatus,
    val message: String,
    val tournament: Tournament?
)

enum class ServiceStatus {
    CREATED, CONFLICT, ACCEPTED, NOT_FOUND, NO_CONTENT, OK
}