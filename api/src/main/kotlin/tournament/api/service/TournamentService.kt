package tournament.api.service

import tournament.api.repository.Tournament
import tournament.api.repository.TournamentEntityRepository
import javax.inject.Singleton

interface TournamentService {
    fun getTournamentById(id: String): Tournament?
    fun getTournaments(): List<Tournament>
}

@Singleton
class DefaultTournamentService(private val repository: TournamentEntityRepository): TournamentService {
    override fun getTournamentById(id: String): Tournament? {
        return repository.getTournamentById(id)
    }

    override fun getTournaments(): List<Tournament> {
        return repository.getAllTournaments()
    }

}