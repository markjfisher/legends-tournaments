package tournament.api.controller

import io.micronaut.http.HttpResponse
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

    @Get("/list")
    fun getTournaments(): Single<List<Tournament>> {
        return Single.just(service.getTournaments())
    }

    @Get("/{id}")
    fun getTournament(id: String): Maybe<Tournament> {
        val tournament = service.getTournamentById(id)
        return if (tournament != null) Maybe.just(tournament) else Maybe.empty()
    }


    @Post("/create")
    fun saveTournament(@Body @Valid tournament: Single<Tournament>): Single<HttpResponse<Tournament>> {
        return tournament.map { t ->
            try {
                val saved = service.saveTournament(t)
                HttpResponse.created(saved)
            } catch (e: Exception) {
                HttpResponse.serverError(t)
            }
        }
    }


}