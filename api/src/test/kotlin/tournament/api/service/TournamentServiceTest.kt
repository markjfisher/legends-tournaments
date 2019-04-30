package tournament.api.service

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
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
}