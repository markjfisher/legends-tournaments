package tournament

import pl.treksoft.kvision.core.Background
import pl.treksoft.kvision.core.BgRepeat
import pl.treksoft.kvision.core.BgSize
import pl.treksoft.kvision.core.Display
import pl.treksoft.kvision.hmr.ApplicationBase
import pl.treksoft.kvision.html.H4.Companion.h4
import pl.treksoft.kvision.html.Link.Companion.link
import pl.treksoft.kvision.html.P.Companion.p
import pl.treksoft.kvision.i18n.DefaultI18nManager
import pl.treksoft.kvision.i18n.I18n
import pl.treksoft.kvision.panel.FlexJustify
import pl.treksoft.kvision.panel.HPanel.Companion.hPanel
import pl.treksoft.kvision.panel.Root
import pl.treksoft.kvision.panel.VPanel.Companion.vPanel
import pl.treksoft.kvision.require
import pl.treksoft.kvision.utils.pc
import tournament.html.AlertDiv.Companion.createAlertDiv
import tournament.html.NavBar.Companion.createNavBar
import tournament.html.Panel.Companion.createPanel
import tournament.html.PanelType

object Tournament : ApplicationBase {
    private lateinit var root: Root

    override fun start(state: Map<String, Any>) {
        I18n.manager =
            DefaultI18nManager(
                mapOf(
                    "en" to require("./messages-en.json")
                )
            )

        root = Root("tournament") {
            background = Background(
                image = "/images/mech-dragon.jpg",
                size = BgSize.COVER,
                repeat = BgRepeat.NOREPEAT
            )
            height = 100.pc
            width = 100.pc

            vPanel(justify = FlexJustify.CENTER, spacing = 10) {

                createNavBar()

                hPanel(justify = FlexJustify.CENTER) {

                    createPanel(
                        id = "primary-panel",
                        title = "Primary Title",
                        content = "The content of the primary panel which may span over multiple lines, but have we restricted it?"
                    )
                    createPanel(
                        title = "Success Title",
                        content = "The content of the success",
                        type = PanelType.SUCCESS
                    )
                    createPanel(
                        title = "Warning Title",
                        content = "The content of the warning",
                        type = PanelType.WARNING
                    )
                    createPanel(
                        title = "Danger Title",
                        content = "The content of the danger",
                        type = PanelType.DANGER
                    )
                    createPanel(
                        title = "Info Title",
                        content = "The content of the info",
                        type = PanelType.INFO
                    )
                }

                vPanel(justify = FlexJustify.CENTER) {
                    createAlertDiv(buttonId = "btn-warning") {
                        h4(content = "Warning!")
                        p(rich = true, content = "<strong>Damn it!!</strong> ") {
                            display = Display.INLINE
                        }
                        link(label = "Change a few things up", url = "#!/basic", classes = setOf("alert-link")) {
                            display = Display.INLINE
                        }
                        p(content = " and try again") {
                            display = Display.INLINE
                        }
                    }
                }
            }
        }
    }

    override fun dispose(): Map<String, Any> {
        root.dispose()
        return mapOf()
    }

    val css = require("./css/tournament.css")
}
