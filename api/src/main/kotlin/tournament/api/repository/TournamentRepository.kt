package tournament.api.repository

interface TournamentRepository {
    fun getTournaments(): List<Tournament>
    fun getTournamentById(id: String): Tournament?
    fun getTournamentByName(name: String): Tournament?
    fun saveTournament(tournament: Tournament): ServiceResult
    fun deleteTournament(id: String): ServiceStatus
    fun updateTournament(tournament: Tournament): ServiceResult
}