import assertk.assertThat
import assertk.assertions.isEqualTo
import pl.treksoft.jquery.jQuery
import tournament.Tournament
import kotlin.test.Test

class TournamentTest : DomTest {

    @Test
    fun aPrimaryPanelIsCreated() {
        run {
            Tournament.start((mapOf()))
            val panel = jQuery("div#primary-panel")
            val elements = panel.get()
            assertThat(elements.size).isEqualTo(1)
            assertThat(elements.first().id).isEqualTo("primary-panel")
            console.log(elements.first().id)
        }
    }

    @Test
    fun canCloseAnAlert() {
        run {
            Tournament.start((mapOf()))
            assertThat(jQuery("#btn-warning").get().size).isEqualTo(1)
            jQuery("#btn-warning").first().click()
            assertThat(jQuery("#btn-warning").get().size).isEqualTo(0)
        }
    }
}
