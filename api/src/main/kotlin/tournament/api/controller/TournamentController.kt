package tournament.api.controller

import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.validation.Validated
import io.reactivex.Maybe
import io.reactivex.Single
import tournament.api.repository.Tournament
import tournament.api.service.TournamentService
import javax.annotation.security.PermitAll
import javax.validation.Valid

@Validated
@PermitAll
@Controller("/tournament")
class TournamentController(private val service: TournamentService) {

    @Get("/")
    fun listTournaments(): Single<List<Tournament>> {
        return Single.just(service.getTournaments())
    }

    @Get("/{id}")
    fun getTournament(id: String): Maybe<Tournament> {
        val tournament = service.getTournamentById(id)
        return if (tournament != null) Maybe.just(tournament) else Maybe.empty()
    }


    @Post("/")
    fun createTournament(@Body @Valid tournament: Single<Tournament>): Single<MutableHttpResponse<Tournament>> {
        return tournament
            .map { t ->
                val (status, saved) = service.saveTournament(t)
                when {
                    status.httpStatus == HttpStatus.OK -> HttpResponse.created(saved)
                    status.httpStatus == HttpStatus.CONFLICT -> HttpResponse.status<Tournament>(status.httpStatus, status.message)
                    else -> HttpResponse.badRequest(t)
                }
            }
            .onErrorResumeNext { t: Throwable ->
                Single.error(ApiException("Could not save tournament", t))
            }
    }

}

class ApiException(message: String? = null, cause: Throwable) : Exception(message, cause) {
    constructor(cause: Throwable) : this(null, cause)
}