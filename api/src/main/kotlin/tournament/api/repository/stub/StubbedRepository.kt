package tournament.api.repository.stub

import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpStatus
import tournament.api.repository.ReturnStatus
import tournament.api.repository.Tournament
import tournament.api.repository.TournamentRepository
import javax.inject.Singleton

@Singleton
@Requires(env = ["stubbed"])
class StubbedRepository : TournamentRepository {
    companion object {
        private val tournamentData = listOf(
            Tournament(id = "1", name = "tournament 1", rules = listOf("epicCount = 3", "legendCount = 1", "cardCount = 50")),
            Tournament(id = "2", name = "tournament 2", rules = listOf("epicCount = 5", "legendCount = 5", "cardCount = 75"))
        )
    }
    override fun getTournaments(): List<Tournament> {
        return tournamentData
    }

    override fun getTournamentById(id: String): Tournament? {
        return tournamentData.find { it.id == id }
    }

    override fun getTournamentByName(name: String): Tournament? {
        return tournamentData.find { it.name.toLowerCase() == name.toLowerCase() }
    }

    override fun saveTournament(tournament: Tournament): Pair<ReturnStatus, Tournament> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteTournament(id: String): HttpStatus {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateTournament(tournament: Tournament): Pair<ReturnStatus, Tournament> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}