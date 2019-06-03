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
import tournament.api.repository.ServiceStatus
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


    // TODO: convert to Single<String> on input
    @Get("/{id}")
    fun getTournament(id: String): Maybe<Tournament> {
        val tournament = service.getTournamentById(id)
        return if (tournament != null) Maybe.just(tournament) else Maybe.empty()
    }

    @Post("/")
    fun createTournament(@Body @Valid tournament: Single<Tournament>): Single<MutableHttpResponse<String>> {
        return tournament
            .map { t ->
                val result = service.saveTournament(t)
                when {
                    result.serviceStatus == ServiceStatus.CREATED -> HttpResponse.created(result.tournament!!.asJson())
                    result.serviceStatus == ServiceStatus.CONFLICT -> HttpResponse.status(HttpStatus.CONFLICT, result.message)
                    else -> HttpResponse.badRequest("Could not create tournament: ${result.message}")
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
                    ServiceStatus.ACCEPTED -> HttpResponse.accepted()
                    ServiceStatus.NOT_FOUND -> HttpResponse.notFound()
                    else -> HttpResponse.badRequest<String>("Could not delete tournament.")
                }
            }
            .onErrorResumeNext { t: Throwable ->
                Single.just(HttpResponse.serverError("Could not delete tournament: ${t.message}"))
            }
    }

    @Put("/")
    fun updateOrCreateTournament(@Body @Valid tournament: Single<Tournament>): Single<MutableHttpResponse<String>> {
        return tournament
            .map { t ->
                val result = service.updateTournament(t)
                when (result.serviceStatus) {
                    ServiceStatus.NO_CONTENT -> HttpResponse.noContent()
                    ServiceStatus.CREATED -> HttpResponse.created(result.tournament!!.asJson()) // it was saved instead of updated, return the body of the item
                    ServiceStatus.CONFLICT -> HttpResponse.badRequest("Duplicate tournament name found with different id")
                    else -> HttpResponse.badRequest<String>()
                }
            }
            .onErrorResumeNext { t: Throwable ->
                Single.just(HttpResponse.serverError("Could not update tournament: ${t.message}"))
            }

    }
}
