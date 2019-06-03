package tournament.api.repository

import io.micronaut.http.HttpStatus

interface TournamentRepository {
    fun getTournaments(): List<Tournament>
    fun getTournamentById(id: String): Tournament?
    fun getTournamentByName(name: String): Tournament?
    fun saveTournament(tournament: Tournament): Pair<ReturnStatus, Tournament>
    fun deleteTournament(id: String): HttpStatus
    fun updateTournament(tournament: Tournament): Pair<ReturnStatus, Tournament>
}