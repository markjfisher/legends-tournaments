package tournament.api.repository.simple

import io.micronaut.context.annotation.Requires
import tournament.api.repository.ServiceResult
import tournament.api.repository.ServiceStatus
import tournament.api.repository.Tournament
import tournament.api.repository.TournamentRepository
import tournament.api.util.replace
import java.time.Instant
import java.util.*
import javax.inject.Singleton

@Singleton
@Requires(env = ["simple"])
class SimpleRepository : TournamentRepository {
    companion object {
        private val tournamentData = mutableListOf(
            Tournament(
                id = "1",
                name = "tournament 1",
                cardImage = "https://www.legends-decks.com/img_cards/cauterize.png",
                rules = listOf("epicCount == 3", "legendCount == 1", "cardCount == 50"),
                date = Date.from(Instant.parse("1969-03-24T06:00:00.0000Z"))
            ),
            Tournament(
                id = "2",
                name = "tournament 2",
                cardImage = "https://www.legends-decks.com/img_cards/baandariopportunist.png",
                rules = listOf("epicCount == 5", "legendCount == 5", "cardCount == 75"),
                date = Date.from(Instant.parse("1973-04-12T12:00:00.0000Z"))
            )
        )
    }

    override fun getTournaments(): List<Tournament> {
        return tournamentData.map { it }
    }

    override fun getTournamentById(id: String): Tournament? {
        return tournamentData.find { it.id == id }
    }

    override fun getTournamentByName(name: String): Tournament? {
        return tournamentData.find { it.name.toLowerCase() == name.toLowerCase() }
    }

    override fun saveTournament(tournament: Tournament): ServiceResult {
        val existingTournament = getTournamentByName(tournament.name)
        return if (existingTournament != null) {
            ServiceResult(
                message = "Found existing tournament with name ${tournament.name}",
                serviceStatus = ServiceStatus.CONFLICT,
                tournament = tournament
            )
        } else {
            val newTournament = Tournament(id = "${tournamentData.size + 1}", name = tournament.name, rules = tournament.rules)
            tournamentData.add(newTournament)
            ServiceResult(message = "", serviceStatus = ServiceStatus.CREATED, tournament = newTournament)
        }
    }

    override fun deleteTournament(id: String): ServiceStatus {
        val tournament = getTournamentById(id) ?: return ServiceStatus.NOT_FOUND
        tournamentData.remove(tournament)
        return ServiceStatus.ACCEPTED
    }

    override fun updateTournament(tournament: Tournament): ServiceResult {
        getTournamentById(tournament.id) ?: return saveTournament(tournament)
        tournamentData.replace(tournament) { it.id == tournament.id }
        return ServiceResult(message = "", serviceStatus = ServiceStatus.NO_CONTENT, tournament = tournament)
    }
}
