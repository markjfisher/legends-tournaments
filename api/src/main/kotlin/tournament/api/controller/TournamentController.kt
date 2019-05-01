package tournament.api.controller

import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.*
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
    fun createTournament(@Body @Valid tournament: Single<Tournament>): Single<MutableHttpResponse<String>> {
        return tournament
            .map { t ->
                val (status, saved) = service.saveTournament(t)
                when {
                    status.httpStatus == HttpStatus.OK -> HttpResponse.created(saved.asJson())
                    status.httpStatus == HttpStatus.CONFLICT -> HttpResponse.status(status.httpStatus, status.message)
                    else -> HttpResponse.badRequest("Could not create tournament: ${status.message}")
                }
            }
            .onErrorResumeNext { t: Throwable ->
                Single.just(HttpResponse.serverError("Could not create tournament: ${t.message}"))
            }
    }

    @Delete("/{id}")
    fun deleteTournament(id: Single<String>): Single<MutableHttpResponse<String>> {
        return id
            .map { v ->
                when (service.deleteTournament(v)) {
                    HttpStatus.ACCEPTED -> HttpResponse.accepted()
                    HttpStatus.NOT_FOUND -> HttpResponse.notFound()
                    else -> HttpResponse.badRequest<String>("Could not delete tournament.")
                }
            }
            .onErrorResumeNext { t: Throwable ->
                Single.just(HttpResponse.serverError("Could not delete tournament: ${t.message}"))
            }
    }
}
