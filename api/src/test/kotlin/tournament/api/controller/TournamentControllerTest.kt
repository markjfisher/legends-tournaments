package tournament.api.controller

import io.micronaut.http.HttpStatus
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import tournament.api.repository.Tournament
import tournament.api.service.TournamentService
import java.io.IOException
import java.lang.RuntimeException

class TournamentControllerTest {

    private val tournament =
        Tournament(
            id = "1",
            name = "Test Tournament",
            date = "2019-30-04 00:00:00.0000Z",
            rules = listOf("rule 1", "rule 2")
        )

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

    @Test
    fun `should save and return tournament when posted to controller`() {
        val responseTournament = Tournament(id = "2", name = "Test Tournament Response")
        val service = mockk<TournamentService>(relaxed = false)
        every { service.saveTournament(any()) } returns responseTournament

        // When
        val response = TournamentController(service).saveTournament(Single.just(tournament)).blockingGet()

        // then
        verify(exactly = 1) { service.saveTournament(any()) }
        assertThat(response.status).isEqualTo(HttpStatus.CREATED)
        assertThat(response.body()).isEqualTo(responseTournament)
    }

    @Test
    fun `should return server error when service throws errors`() {
        val service = mockk<TournamentService>(relaxed = false)
        every { service.saveTournament(any()) } throws IOException("Test Error")

        val response = TournamentController(service).saveTournament(Single.just(tournament)).blockingGet()

        verify(exactly = 1) { service.saveTournament(any()) }
        assertThat(response.status).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
        assertThat(response.body()).isEqualTo(tournament)
    }

}