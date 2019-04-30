package tournament.api.service

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import tournament.api.repository.Tournament
import tournament.api.repository.TournamentEntityRepository

internal class TournamentServiceTest {
    private val repository = mockk<TournamentEntityRepository>()

    @Test
    fun `when no id matches a null tournament is returned`() {
        every { repository.getTournamentById(any()) } returns null

        // When
        val tournament = DefaultTournamentService(repository).getTournamentById("any")

        // Then
        assertThat(tournament).isNull()
    }

    @Test
    fun `when there are no tournaments in the repo an empty list is returned`() {
        every { repository.getAllTournaments() } returns emptyList()

        // When
        val tournaments = DefaultTournamentService(repository).getTournaments()

        // Then
        assertThat(tournaments).isEmpty()
    }

    @Test
    fun `the returned tournaments from the repository is returned by service`() {
        val listOfTournaments = listOf(Tournament(id = "1", name = "Test Tournament 1"), Tournament(id = "2", name = "Test Tournament 2"))
        every { repository.getAllTournaments() } returns listOfTournaments

        // When
        val tournaments = DefaultTournamentService(repository).getTournaments()

        // Then
        assertThat(tournaments).isEqualTo(listOfTournaments)
    }

    @Test
    fun `when a tournament is saved in the repo it is also returned by the service`() {
        val testTournament = Tournament(id = "1", name = "Test Tournament 1")
        every { repository.saveTournament(any()) } returns testTournament

        // When
        val tournament = DefaultTournamentService(repository).saveTournament(testTournament)

        assertThat(tournament).isEqualTo(testTournament)
    }
}