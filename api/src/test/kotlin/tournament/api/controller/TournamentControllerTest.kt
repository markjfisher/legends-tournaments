package tournament.api.controller

import io.micronaut.http.HttpStatus
import io.micronaut.http.netty.NettyMutableHttpResponse
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import tournament.api.repository.ReturnStatus
import tournament.api.repository.Tournament
import tournament.api.service.TournamentService
import java.io.IOException
import java.lang.RuntimeException
import java.time.Instant

class TournamentControllerTest {

    private val tournament =
        Tournament(
            id = "1",
            name = "Test Tournament",
            date = Instant.parse("2019-04-30T00:00:00.0000Z"),
            rules = listOf("rule 1", "rule 2")
        )

    @Test
    fun `should return a list of tournaments`() {
        // Given
        val service = mockk<TournamentService>(relaxed = false)
        every { service.getTournaments() } returns listOf(Tournament(id = "1", name = "test 1"))

        // When
        val tournaments = TournamentController(service).listTournaments().blockingGet()

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
        every { service.saveTournament(any()) } returns createPair(responseTournament)

        // When
        val response = TournamentController(service).createTournament(Single.just(tournament)).blockingGet()

        // then
        verify(exactly = 1) { service.saveTournament(any()) }
        assertThat(response.status).isEqualTo(HttpStatus.CREATED)
        assertThat(response.body()).isEqualTo(responseTournament.asJson())
    }

    @Test
    fun `should do something for Conflict`() {
        val service = mockk<TournamentService>(relaxed = false)
        every { service.saveTournament(any()) } returns Pair(
            ReturnStatus(message = "save error message", httpStatus = HttpStatus.CONFLICT),
            tournament
        )

        val response = TournamentController(service).createTournament(Single.just(tournament)).blockingGet()

        verify(exactly = 1) { service.saveTournament(any()) }
        assertThat(response.status).isEqualTo(HttpStatus.CONFLICT)
        assertThat(response.body()).isNull()
        assertThat((response as NettyMutableHttpResponse).nativeResponse.status().reasonPhrase()).isEqualTo("save error message")
    }

    @Test
    fun `should return server error when service throws errors`() {
        val service = mockk<TournamentService>(relaxed = false)
        every { service.saveTournament(any()) } throws IOException("A test error")

        // When
        val response = TournamentController(service).createTournament(Single.just(tournament)).blockingGet()

        // Then
        verify(exactly = 1) { service.saveTournament(any()) }
        assertThat(response.status).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
        assertThat(response.body()).isEqualTo("Could not create tournament: A test error")

    }

    @Test
    fun `sucessfully deleting a tournament`() {
        val id = "1"
        val service = mockk<TournamentService>(relaxed = false)
        every { service.deleteTournament(id) } returns HttpStatus.ACCEPTED

        // When
        val response = TournamentController(service).deleteTournament(Single.just(id)).blockingGet()

        // Then
        verify(exactly = 1) { service.deleteTournament(any()) }
        assertThat(response.status).isEqualTo(HttpStatus.ACCEPTED)
        assertThat(response.body()).isNull()

    }

    @Test
    fun `could not find tournament to delete`() {
        val id = "1"
        val service = mockk<TournamentService>(relaxed = false)
        every { service.deleteTournament(id) } returns HttpStatus.NOT_FOUND

        // When
        val response = TournamentController(service).deleteTournament(Single.just(id)).blockingGet()

        // Then
        verify(exactly = 1) { service.deleteTournament(any()) }
        assertThat(response.status).isEqualTo(HttpStatus.NOT_FOUND)
        assertThat(response.body()).isNull()

    }

    @Test
    fun `delete causes error in service`() {
        val id = "1"
        val service = mockk<TournamentService>(relaxed = false)
        every { service.deleteTournament(id) } throws IOException("A test error")

        // When
        val response = TournamentController(service).deleteTournament(Single.just(id)).blockingGet()

        // Then
        verify(exactly = 1) { service.deleteTournament(any()) }
        assertThat(response.status).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
        assertThat(response.body()).isEqualTo("Could not delete tournament: A test error")

    }

    private fun <T> createPair(item: T, httpStatus: HttpStatus = HttpStatus.OK, message: String = ""): Pair<ReturnStatus, T> {
        return Pair(ReturnStatus(message = message, httpStatus = httpStatus), item)
    }

}