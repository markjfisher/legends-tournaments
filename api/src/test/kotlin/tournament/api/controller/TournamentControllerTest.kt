package tournament.api.controller

import io.micronaut.http.HttpStatus
import io.micronaut.http.netty.NettyMutableHttpResponse
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import tournament.api.createResult
import tournament.api.repository.ServiceResult
import tournament.api.repository.ServiceStatus
import tournament.api.repository.Tournament
import tournament.api.service.TournamentService
import java.io.IOException
import java.time.Instant
import java.util.*

class TournamentControllerTest {

    private val tournament =
        Tournament(
            id = "1",
            name = "Test Tournament",
            date = Date.from(Instant.parse("2019-04-30T00:00:00.0000Z")),
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
        every { service.saveTournament(any()) } returns createResult(item = responseTournament, httpStatus = ServiceStatus.CREATED)

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
        every { service.saveTournament(any()) } returns createResult(item = tournament, httpStatus = ServiceStatus.CONFLICT, message = "save error message")

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
        every { service.deleteTournament(id) } returns ServiceStatus.ACCEPTED

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
        every { service.deleteTournament(id) } returns ServiceStatus.NOT_FOUND

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

    @Test
    fun `sucessfully updating a tournament`() {
        val service = mockk<TournamentService>(relaxed = false)
        every { service.updateTournament(any()) } returns ServiceResult(message = "", serviceStatus = ServiceStatus.NO_CONTENT, tournament = tournament)

        // When
        val response = TournamentController(service).updateOrCreateTournament(Single.just(tournament)).blockingGet()

        // Then
        verify(exactly = 1) { service.updateTournament(any()) }
        assertThat(response.status).isEqualTo(HttpStatus.NO_CONTENT)
        assertThat(response.body()).isNull()

    }


    @Test
    fun `sucessfully putting a new tournament is same as saving`() {
        val service = mockk<TournamentService>(relaxed = false)
        every { service.updateTournament(any()) } returns ServiceResult(message = "", serviceStatus = ServiceStatus.CREATED, tournament = tournament)

        // When
        val response = TournamentController(service).updateOrCreateTournament(Single.just(tournament)).blockingGet()

        // Then
        verify(exactly = 1) { service.updateTournament(any()) }
        assertThat(response.status).isEqualTo(HttpStatus.CREATED)
        assertThat(response.body()).isEqualTo(tournament.asJson())

    }

    @Test
    fun `putting a duplicate named tournament is a conflict`() {
        val service = mockk<TournamentService>(relaxed = false)
        every { service.updateTournament(any()) } returns ServiceResult(message = "", serviceStatus = ServiceStatus.CONFLICT, tournament = tournament)

        // When
        val response = TournamentController(service).updateOrCreateTournament(Single.just(tournament)).blockingGet()

        // Then
        verify(exactly = 1) { service.updateTournament(any()) }
        assertThat(response.status).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(response.body()).isEqualTo("Duplicate tournament name found with different id")

    }

    @Test
    fun `update causes error in service`() {
        val service = mockk<TournamentService>(relaxed = false)
        every { service.updateTournament(any()) } throws IOException("A test error")

        // When
        val response = TournamentController(service).updateOrCreateTournament(Single.just(tournament)).blockingGet()

        // Then
        verify(exactly = 1) { service.updateTournament(any()) }
        assertThat(response.status).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
        assertThat(response.body()).isEqualTo("Could not update tournament: A test error")

    }

}