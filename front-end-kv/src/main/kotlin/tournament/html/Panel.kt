package tournament.html

import pl.treksoft.kvision.core.Container
import pl.treksoft.kvision.html.Div
import pl.treksoft.kvision.html.Div.Companion.div
import pl.treksoft.kvision.html.H3.Companion.h3
import pl.treksoft.kvision.html.H4.Companion.h4
import pl.treksoft.kvision.html.P.Companion.p
import pl.treksoft.kvision.utils.rem

class Panel {
    companion object {
        fun Container.createPanel(id: String? = null, title: String, content: String, type: PanelType = PanelType.PRIMARY): Div {
            return div(classes = setOf("panel", type.className)) {
                if (id != null) setAttribute("id", id)
                maxWidth = 20.rem
                div(classes = setOf("panel-heading")) {
                    h3(content = title, classes = setOf("panel-title"))
                }
                div(content = content, classes = setOf("panel-body"))
            }
        }

    }
}

enum class PanelType(val className: String) {
    PRIMARY("panel-primary"),
    SUCCESS("panel-success"),
    DANGER("panel-danger"),
    WARNING("panel-warning"),
    INFO("panel-info")
}