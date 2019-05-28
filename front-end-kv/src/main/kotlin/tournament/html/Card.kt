package tournament.html

import pl.treksoft.kvision.core.Container
import pl.treksoft.kvision.html.Div
import pl.treksoft.kvision.html.Div.Companion.div
import pl.treksoft.kvision.html.H4.Companion.h4
import pl.treksoft.kvision.html.P.Companion.p
import pl.treksoft.kvision.utils.rem

data class Card(
    val cardHeader: String = "",
    val cardTitle: String = "",
    val cardText: String = "",
    val cardType: CardType = CardType.PRIMARY,
    val mainTextColour: String = "text-white"
) {
    companion object {
        fun Container.createCard(card: Card): Div {
            console.log("card: $card")
            return div(classes = setOf("card", "mb-3", card.mainTextColour, card.cardType.className)) {
                maxWidth = 20.rem
                div(content = card.cardHeader, classes = setOf("card-header"))
                div(classes = setOf("card-body")) {
                    h4(content = card.cardTitle, classes = setOf("card-title"))
                    p(content = card.cardText, classes = setOf("card-text"))
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