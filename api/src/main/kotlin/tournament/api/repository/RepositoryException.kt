package tournament.api.repository

// This extends a RuntimeException as that is what Micronaut is catching when using Retryable annotation
open class RepositoryException(cause: Throwable?) : RuntimeException(cause)
