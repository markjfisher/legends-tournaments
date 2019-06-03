package tournament.html

import pl.treksoft.kvision.core.Container
import pl.treksoft.kvision.html.Div
import pl.treksoft.kvision.html.Div.Companion.div
import pl.treksoft.kvision.html.H4.Companion.h4
import pl.treksoft.kvision.html.P.Companion.p
import pl.treksoft.kvision.utils.rem

// This needs implementing in CSS as it's not part of Bootswatch 3

class Card {
    companion object {
        fun Container.createCard(
            header: String,
            title: String,
            text: String,
            mainTextColour: String,
            type: CardType = CardType.PRIMARY
        ): Div {
            return div(classes = setOf("card", "mb-3", mainTextColour, type.className)) {
                maxWidth = 20.rem
                div(content = header, classes = setOf("card-header"))
                div(classes = setOf("card-body")) {
                    h4(content = title, classes = setOf("card-title"))
                    p(content = text, classes = setOf("card-text"))
                }
            }
        }
    }
}

enum class CardType(val className: String) {
    PRIMARY("bg-primary"),
    SECONDARY("bg-secondary"),
    SUCCESS("bg-success"),
    DANGER("bg-danger"),
    WARNING("bg-warning"),
    INFO("bg-info"),
    LIGHT("bg-light"),
    DARK("bg-dark")
}