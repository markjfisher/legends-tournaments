package tournament.api.service

import tournament.api.repository.ServiceResult
import tournament.api.repository.ServiceStatus
import tournament.api.repository.Tournament
import tournament.api.repository.TournamentRepository
import javax.inject.Singleton

interface TournamentService {
    fun getTournamentById(id: String): Tournament?
    fun getTournaments(): List<Tournament>
    fun saveTournament(tournament: Tournament): ServiceResult
    fun deleteTournament(id: String): ServiceStatus
    fun updateTournament(tournament: Tournament): ServiceResult
}

@Singleton
class DefaultTournamentService(private val repository: TournamentRepository): TournamentService {
    override fun getTournamentById(id: String): Tournament? {
        return repository.getTournamentById(id)
    }

    override fun getTournaments(): List<Tournament> {
        return repository.getTournaments()
    }

    override fun saveTournament(tournament: Tournament): ServiceResult {
        return repository.saveTournament(tournament)
    }

    override fun deleteTournament(id: String): ServiceStatus {
        return repository.deleteTournament(id)
    }

    override fun updateTournament(tournament: Tournament): ServiceResult {
        return repository.updateTournament(tournament)
    }
}