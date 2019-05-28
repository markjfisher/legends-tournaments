package tournament.api.kunafa

import com.narbase.kunafa.core.components.horizontalLayout
import com.narbase.kunafa.core.components.page
import com.narbase.kunafa.core.components.verticalLayout
import com.narbase.kunafa.core.css.backgroundImage
import com.narbase.kunafa.core.css.backgroundPosition
import com.narbase.kunafa.core.css.backgroundRepeat
import com.narbase.kunafa.core.css.backgroundSize
import com.narbase.kunafa.core.routing.route
import kotlinx.serialization.UnstableDefault
import tournament.api.kunafa.view.Home

@UnstableDefault
fun main() {
    page {
        // mount(TournamentComponent(TournamentViewModel()))
        style {
            backgroundImage = "url('images/mech-dragon.jpg')"
            backgroundPosition = "centre"
            backgroundRepeat = "no-repeat"
            backgroundSize = "cover"
        }

        // mount(TournamentComponent(TournamentViewModel()))
        verticalLayout {
            route("/", isExact = true) {
                horizontalLayout {
                    mount(Home())
                }
            }
        }
    }

}
