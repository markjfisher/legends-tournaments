package tournament.api.controller

import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.annotation.MicronautTest
import io.micronaut.test.annotation.MockBean
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import tournament.api.createPair
import tournament.api.repository.Tournament
import tournament.api.service.DefaultTournamentService
import tournament.api.service.TournamentService
import java.time.Instant
import javax.inject.Inject

@MicronautTest
class TournamentAPITest {
    @Inject
    @field:Client("/")
    lateinit var client: RxHttpClient

    @Inject
    lateinit var service: TournamentService

    @MockBean(DefaultTournamentService::class)
    fun service(): TournamentService {
        return mockk()
    }

    private val tournament =
        Tournament(
            id = "1",
            name = "Test Tournament",
            date = Instant.parse("2019-04-30T00:00:00.0000Z"),
            rules = listOf("rule 1", "rule 2")
        )

    @Test
    fun `should return a tournament shaped response`() {
        // Given
        every { service.getTournamentById(any()) } returns tournament

        // When
        val response = client.toBlocking().retrieve("/tournament/1")

        // Then
        assertThat(response).isEqualToIgnoringWhitespace("""
            {"id":"1","name":"Test Tournament","date":1556582400.000000000,"rules":["rule 1","rule 2"]}
        """.trimIndent())
    }

    @Test
    fun `should return a tournament for a known id`() {
        val tournamentId = "1"
        val capturedId = slot<String>()
        // Given
        every { service.getTournamentById(id = capture(capturedId))} returns tournament

        val request: HttpRequest<String> = HttpRequest.GET("/tournament/$tournamentId")
        val response = client.toBlocking().exchange(request, Argument.of(Tournament::class.java))

        // Then
        assertThat(capturedId.captured).isEqualTo(tournamentId)

        with(response) {
            assertThat(status).isEqualTo(HttpStatus.OK)
            assertThat(headers.contentType.get()).isEqualTo("application/json")
            assertThat(body.get()).isEqualTo(tournament)
        }
    }

    @Test
    fun `should give not found for missing tournament`() {
        // Given
        every { service.getTournamentById(any())} returns null

        val exception = assertThrows<HttpClientResponseException> {
            client.toBlocking().retrieve("/tournament/missing")
        }

        assertThat(exception.status).isEqualTo(HttpStatus.NOT_FOUND)
        assertThat(exception.message).isEqualTo("Page Not Found")
    }

    @Test
    fun `should return list of tournaments in json format`() {
        // Given
        every { service.getTournaments() } returns listOf(tournament)

        // When
        val response = client.toBlocking().retrieve("/tournament/")

        // Then
        assertThat(response).isEqualToIgnoringWhitespace("""
            [{"id":"1","name":"Test Tournament","date":1556582400.000000000,"rules":["rule 1","rule 2"]}]
        """.trimIndent())
    }

    @Test
    fun `should return list of tournaments`() {
        // Given
        every { service.getTournaments()} returns listOf(tournament)

        val request: HttpRequest<String> = HttpRequest.GET("/tournament/")
        val response = client.toBlocking().exchange(request, Argument.of(List::class.java, Tournament::class.java))

        // Then
        with(response) {
            assertThat(status).isEqualTo(HttpStatus.OK)
            assertThat(headers.contentType.get()).isEqualTo("application/json")
            assertThat(body.get()).isEqualTo(listOf(tournament))
        }
    }

    @Test
    fun `should return empty list when there are no tournaments`() {
        // Given
        every { service.getTournaments()} returns emptyList()

        val request: HttpRequest<String> = HttpRequest.GET("/tournament/")
        val response = client.toBlocking().exchange(request, Argument.of(List::class.java, Tournament::class.java))

        // Then
        with(response) {
            assertThat(status).isEqualTo(HttpStatus.OK)
            assertThat(headers.contentType.get()).isEqualTo("application/json")
            assertThat(body.get()).isEqualTo(emptyList<Tournament>())
        }
    }

    @Test
    fun `should return saved tournament when created`() {
        every { service.saveTournament(any()) } returns createPair(item = tournament, httpStatus = HttpStatus.CREATED)

        val request: HttpRequest<Tournament>? = HttpRequest.POST("/tournament/", tournament)
        val response = client.toBlocking().exchange(request, Argument.of(Tournament::class.java))

        with(response) {
            assertThat(status).isEqualTo(HttpStatus.CREATED)
            assertThat(headers.contentType.get()).isEqualTo("application/json")
            assertThat(body.get()).isEqualTo(tournament)
        }
    }

    // TODO: Delete, Put

    // @Disabled("TODO: Work out why this passes - validation on Rx types not working? it should fail NotEmpty validation on bean")
    @Test
    fun `should fail gracefully when validation fails`() {
        val incompleteTournament = Tournament(id = "x")
        every { service.saveTournament(any()) } returns createPair(item = incompleteTournament, httpStatus = HttpStatus.CREATED)

        val request: HttpRequest<Tournament>? = HttpRequest.POST("/tournament/", incompleteTournament)
        val response = client.toBlocking().exchange(request, Argument.of(Tournament::class.java))

        with(response) {
            assertThat(status).isEqualTo(HttpStatus.CREATED)
            assertThat(headers.contentType.get()).isEqualTo("application/json")
            assertThat(body.get()).isEqualTo(incompleteTournament)
        }
    }

}