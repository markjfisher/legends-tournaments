package tournament.api.repository

// This extends a RuntimeException as that is what Micronaut is catching when using Retryable annotation
// open class RepositoryException(cause: Throwable?) : RuntimeException(cause)

class RepositoryException : RuntimeException {
    constructor(message: String, cause: Throwable): super(message, cause)
    constructor(message: String): super(message)
    constructor(cause: Throwable): super(cause)
}