package tournament.api.controller

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import tournament.api.repository.Tournament
import tournament.api.service.TournamentService

class TournamentControllerTest {
    @Test
    fun `should return a list of tournaments`() {
        // Given
        val service = mockk<TournamentService>(relaxed = false)
        every { service.getTournaments() } returns listOf(Tournament(id = "1", name = "test 1"))

        // When
        val tournaments = TournamentController(service).getTournaments().blockingGet()

        // Then
        assertThat(tournaments).isNotEmpty
        assertThat(tournaments.size).isEqualTo(1)
        assertThat(tournaments.first().id).isEqualTo("1")
        assertThat(tournaments.first().name).isEqualTo("test 1")
    }

    @Test
    fun `should return a single tournament by id`() {
        val service = mockk<TournamentService>(relaxed = false)
        every { service.getTournamentById(any()) } returns Tournament(id = "1", name = "test 1")

        // When
        val tournament = TournamentController(service).getTournament("1").blockingGet()

        // Then
        assertThat(tournament).isEqualTo(Tournament(id = "1", name = "test 1"))
    }

    @Test
    fun `should return empty maybe when id doesn't match`() {
        val service = mockk<TournamentService>(relaxed = false)
        every { service.getTournamentById(any()) } returns null

        // When
        val tournament = TournamentController(service).getTournament("1").blockingGet()

        // Then
        assertThat(tournament).isNull()
    }


}