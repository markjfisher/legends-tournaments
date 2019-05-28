import tournament.Tournament
import kotlin.browser.document
import kotlin.test.Test

class TournamentTest : DomTest {

    @Test
    fun render() {
        run {
            Tournament.start((mapOf()))
            val element = document.getElementById("tournament")!!
//            assertThat(element.innerHTML).contains("Hello world!")
        }
    }
}
