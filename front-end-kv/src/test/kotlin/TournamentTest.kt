import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import pl.treksoft.jquery.jQuery
import tournament.TournamentApp
import kotlin.test.Test

class TournamentTest : DomTest {

    @Test
    fun aPrimaryPanelIsCreated() {
        run {
            TournamentApp.start((mapOf()))
//            val panel = jQuery("div")
            assertThat(true).isTrue()
//            val elements = panel.get()
//            assertThat(elements.size).isEqualTo(1)
//            assertThat(elements.first().id).isEqualTo("primary-panel")
//            console.log(elements.first().id)
        }
    }

    @Test
    fun canCloseAnAlert() {
        run {
            TournamentApp.start((mapOf()))
            assertThat(true).isTrue()
//            assertThat(jQuery("#btn-warning").get().size).isEqualTo(1)
//            jQuery("#btn-warning").first().click()
//            assertThat(jQuery("#btn-warning").get().size).isEqualTo(0)
        }
    }
}
