package tournament.api.service

import io.micronaut.http.HttpStatus
import tournament.api.repository.ReturnStatus
import tournament.api.repository.Tournament
import tournament.api.repository.TournamentRepository
import javax.inject.Singleton

interface TournamentService {
    fun getTournamentById(id: String): Tournament?
    fun getTournaments(): List<Tournament>
    fun saveTournament(tournament: Tournament): Pair<ReturnStatus, Tournament>
    fun deleteTournament(id: String): HttpStatus
    fun updateTournament(tournament: Tournament): Pair<ReturnStatus, Tournament>
}

@Singleton
class DefaultTournamentService(private val repository: TournamentRepository): TournamentService {
    override fun getTournamentById(id: String): Tournament? {
        return repository.getTournamentById(id)
    }

    override fun getTournaments(): List<Tournament> {
        return repository.getTournaments()
    }

    override fun saveTournament(tournament: Tournament): Pair<ReturnStatus, Tournament> {
        return repository.saveTournament(tournament)
    }

    override fun deleteTournament(id: String): HttpStatus {
        return repository.deleteTournament(id)
    }

    override fun updateTournament(tournament: Tournament): Pair<ReturnStatus, Tournament> {
        return repository.updateTournament(tournament)
    }
}