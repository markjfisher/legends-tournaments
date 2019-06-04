package tournament.test

import pl.treksoft.kvision.core.Display
import pl.treksoft.kvision.dropdown.DropDown.Companion.dropDown
import pl.treksoft.kvision.form.check.CheckBox.Companion.checkBox
import pl.treksoft.kvision.form.text.Text.Companion.text
import pl.treksoft.kvision.html.H4.Companion.h4
import pl.treksoft.kvision.html.Link.Companion.link
import pl.treksoft.kvision.html.P.Companion.p
import pl.treksoft.kvision.html.TAG
import pl.treksoft.kvision.html.Tag.Companion.tag
import pl.treksoft.kvision.navbar.Nav.Companion.nav
import pl.treksoft.kvision.navbar.NavForm.Companion.navForm
import pl.treksoft.kvision.navbar.Navbar.Companion.navbar
import pl.treksoft.kvision.panel.FlexJustify
import pl.treksoft.kvision.panel.HPanel.Companion.hPanel
import pl.treksoft.kvision.panel.Root
import pl.treksoft.kvision.panel.VPanel.Companion.vPanel
import tournament.html.AlertDiv.Companion.createAlertDiv
import tournament.html.Panel.Companion.createPanel
import tournament.html.PanelType

class OldElements {
    private fun panels() {
        Root("x") {
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

    fun navBar() {
        Root("x") {
            navbar("NavBar") {
                nav {
                    tag(TAG.LI) {
                        link("File", icon = "fa-file")
                    }
                    tag(TAG.LI) {
                        link("Edit", icon = "fa-bars")
                    }
                    dropDown(
                        "Favourites",
                        listOf("Basic formatting" to "#!/basic", "Forms" to "#!/forms"),
                        icon = "fa-star",
                        forNavbar = true
                    )
                }
                navForm {
                    text(label = "Search:")
                    checkBox()
                }
                nav(rightAlign = true) {
                    tag(TAG.LI) {
                        link("System", icon = "fa-windows")
                    }
                }
            }
        }
    }
}