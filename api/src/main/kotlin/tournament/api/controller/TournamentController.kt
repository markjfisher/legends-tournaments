package tournament.api.controller

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.validation.Validated
import io.reactivex.Maybe
import io.reactivex.Single
import tournament.api.repository.Tournament
import tournament.api.service.TournamentService
import javax.annotation.security.PermitAll

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


}