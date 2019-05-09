package tournament.api

import com.narbase.kunafa.core.components.page
import kotlinx.serialization.UnstableDefault

@UnstableDefault
fun main() {
    page {
        mount(TournamentComponent(TournamentViewModel()))
    }

}
