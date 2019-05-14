package tournament.api

import com.narbase.kunafa.core.components.page
import com.narbase.kunafa.core.components.textView
import com.narbase.kunafa.core.components.verticalLayout
import com.narbase.kunafa.core.routing.link
import com.narbase.kunafa.core.routing.route
import kotlinx.serialization.UnstableDefault

@UnstableDefault
fun main() {
    page {
        // mount(TournamentComponent(TournamentViewModel()))
        verticalLayout {
            link("/") { text = "Home" }
            link("/about") { text = "About" }
            route("/", isExact = true) {
                textView {
                    text = "Home page here"
                }
            }
            route("/about") {
                textView {
                    text = "About page"
                }
            }
        }
    }

}
