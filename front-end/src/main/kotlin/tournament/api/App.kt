package tournament.api

import com.narbase.kunafa.core.components.horizontalLayout
import com.narbase.kunafa.core.components.page
import com.narbase.kunafa.core.components.textView
import com.narbase.kunafa.core.components.verticalLayout
import com.narbase.kunafa.core.css.*
import com.narbase.kunafa.core.dimensions.dependent.matchParent
import com.narbase.kunafa.core.dimensions.pc
import com.narbase.kunafa.core.dimensions.px
import com.narbase.kunafa.core.routing.link
import com.narbase.kunafa.core.routing.route
import kotlinx.serialization.UnstableDefault
import tournament.api.view.Home
import tournament.api.view.TournamentComponent
import tournament.api.viewmodel.TournamentViewModel

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
