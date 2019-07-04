package tournament

import kotlinx.serialization.list
import pl.treksoft.kvision.core.*
import pl.treksoft.kvision.form.FormPanel.Companion.formPanel
import pl.treksoft.kvision.form.check.CheckBox
import pl.treksoft.kvision.form.check.Radio
import pl.treksoft.kvision.form.check.RadioGroup
import pl.treksoft.kvision.form.select.Select
import pl.treksoft.kvision.form.spinner.Spinner
import pl.treksoft.kvision.form.text.Password
import pl.treksoft.kvision.form.text.RichText
import pl.treksoft.kvision.form.text.Text
import pl.treksoft.kvision.form.text.Text.Companion.text
import pl.treksoft.kvision.form.text.TextArea
import pl.treksoft.kvision.form.time.DateTime
import pl.treksoft.kvision.form.upload.Upload
import pl.treksoft.kvision.hmr.ApplicationBase
import pl.treksoft.kvision.html.Div
import pl.treksoft.kvision.html.Div.Companion.div
import pl.treksoft.kvision.html.Image.Companion.image
import pl.treksoft.kvision.html.Link.Companion.link
import pl.treksoft.kvision.html.P.Companion.p
import pl.treksoft.kvision.html.Span.Companion.span
import pl.treksoft.kvision.html.TAG
import pl.treksoft.kvision.html.Tag.Companion.tag
import pl.treksoft.kvision.i18n.DefaultI18nManager
import pl.treksoft.kvision.i18n.I18n
import pl.treksoft.kvision.moment.Moment
import pl.treksoft.kvision.navbar.Nav.Companion.nav
import pl.treksoft.kvision.navbar.Navbar
import pl.treksoft.kvision.panel.*
import pl.treksoft.kvision.panel.FlexPanel.Companion.flexPanel
import pl.treksoft.kvision.redux.ActionCreator
import pl.treksoft.kvision.redux.StateBinding.Companion.stateBinding
import pl.treksoft.kvision.redux.createReduxStore
import pl.treksoft.kvision.require
import pl.treksoft.kvision.rest.RestClient
import pl.treksoft.kvision.table.Cell.Companion.cell
import pl.treksoft.kvision.table.Row.Companion.row
import pl.treksoft.kvision.table.Table
import pl.treksoft.kvision.table.Table.Companion.table
import pl.treksoft.kvision.utils.pc
import pl.treksoft.kvision.utils.px
import pl.treksoft.kvision.window.Window.Companion.window
import tournament.html.Routing
import tournament.model.Tournament
import tournament.model.TournamentState
import tournament.model.VIEWMODE
import tournament.redux.TournamentAction
import tournament.redux.tournamentStateReducer

object TournamentApp : ApplicationBase {
    private lateinit var root: Root

    private val store = createReduxStore(
        ::tournamentStateReducer,
        TournamentState(
            viewMode = VIEWMODE.HOME,
            tournaments = emptyList(),
            downloading = false,
            visibleTournaments = emptyList(),
            errorMessage = null,
            searchString = null
        )
    )

    override fun start(state: Map<String, Any>) {
        I18n.manager =
            DefaultI18nManager(
                mapOf(
                    "en" to require("./messages-en.json")
                )
            )

        root = Root("tournament") {
            height = 100.pc

            // createNavBar(this)
            createSidePanel(this)
            flexPanel(direction = FlexDir.COLUMN) {
                addCssClass(Styles.mainStyle)

                createHeader(this)
                createContent(this).stateBinding(store) { state ->
                    when (state.viewMode) {
                        VIEWMODE.HOME -> showHome(this, state)
                        VIEWMODE.LIST_TOURNAMENTS -> listTournaments(this, state)
                        VIEWMODE.FORM -> showForm(this, state)
                    }
                }
            }
        }

        Routing(hash = "#!")
            .on(location = "/", handler = { _ ->
                store.dispatch(TournamentAction.HomePage)
                console.log("dispatched home page")
            })
            .on("/browse", { _ ->
                store.dispatch(TournamentAction.ViewPage)
                console.log("dispatched view page")
            })
            .on("/form", { _ ->
                store.dispatch(TournamentAction.FormPage)
                console.log("dispatched form page")
            })
            .resolve()

        store.dispatch(downloadTournaments())
        store.dispatch(TournamentAction.HomePage)
    }

    private fun createNavBar(container: Container) {
        container.add(Navbar(label = null) {
            marginBottom = 5.px
            nav {
                tag(TAG.LI) {
                    link("Tournaments", url = "#!/")
                }
                tag(TAG.LI) {
                    link("Browse", icon = "fa-trophy", url = "#!/browse")
                }
                tag(TAG.LI) {
                    link("Form", url = "#!/form")
                }
            }
        })
    }

    private fun createHeader(container: Container) {
        container.add(Div {
            background = Background(
                image = "/images/legends-tournaments.png",
                size = BgSize.CONTAIN,
                repeat = BgRepeat.NOREPEAT
            )
            // image aspect is 2500:450 = 18%
            height = 18.pc
        })
    }

    private fun createContent(container: Container): Container {
        val simplePanel = SimplePanel {
            p(content = "content p")
        }
        container.add(simplePanel)
        return simplePanel
    }

    private fun createSidePanel(container: Container) {
        val c = Div {
            addCssClass(Styles.sidePanelStyle)
            text {
                placeholder = "Search tournaments"
                setEventListener<Text> {
                    input = {
                        console.log("input event: ${self.value}")
                        TournamentApp.store.dispatch(TournamentAction.SetSearchString(self.value))
                    }
                }
            }
            link(label = "Home", icon = "fa-home", url = "#!/")
            link(label = "Browse", icon = "fa-trophy", url = "#!/browse")
            link(label = "Form", icon = "fa-trophy", url = "#!/form")
        }
        container.add(c)
    }

    private fun showHome(container: Container, state: TournamentState) {
        val p = VPanel {
            p(content = "home p")
            window(
                contentWidth = 600.px,
                contentHeight = 300.px,
                closeButton = false,
                isDraggable = false,
                isResizable = false
            ) {
                left = 300.px
                top = 300.px
                span("A window content")
            }


        }
        container.add(p)
    }

    private fun showForm(container: Container, state: TournamentState) {
        val p = VPanel {
            p(content = "form p")
            val formPanel = formPanel<MyForm> {
                add(
                    MyForm::text,
                    Text(label = "Text field").apply {
                        placeholder = "Enter text"
                    }
                )

                add(
                    MyForm::password,
                    Password(label = "Password field"),
                    required = true,
                    validatorMessage = { "Enter at least 8 chars" }
                ) {
                    (it.getValue()?.length ?: 0) >= 8
                }

                add(MyForm::textarea, TextArea(label = "Text area field"))
                add(MyForm::richtext, RichText(label = "Rich text field"))
                add(MyForm::date, DateTime(format = "YYYY-MM-DD", label = "Date field"))
                add(MyForm::time, DateTime(format = "HH:mm", label = "Time field"))
                add(MyForm::checkbox, CheckBox(label = "Required checkbox"))
                add(MyForm::radio, Radio(label = "Radio button"))
                add(
                    MyForm::select, Select(
                        options = listOf("first" to "First option", "second" to "Second option"),
                        label = "Simple select"
                    )
                )
                add(MyForm::spinner, Spinner(label = "Spinner field 10 - 20", min = 10, max = 20))
                add(
                    MyForm::radiogroup, RadioGroup(
                        listOf("option1" to "First option", "option2" to "Second option"),
                        inline = true, label = "Radio button group"
                    )
                )
                add(MyForm::upload, Upload("/", multiple = true, label = "Upload files (images only)").apply {
                    explorerTheme = true
                    dropZoneEnabled = false
                    allowedFileTypes = setOf("image")
                })
            }

            val formData = MyForm(
                password = "foo",
                select = "first",
                spinner = 15
            )
            formPanel.setData(formData)
        }
        container.add(p)
    }

    private fun listTournaments(container: Container, state: TournamentState) {
        val p = FlexPanel(
            FlexDir.ROW, FlexWrap.WRAP, FlexJustify.FLEXSTART, FlexAlignItems.CENTER,
            spacing = 5
        )
        state.tournaments.forEach { tournament ->
            createCard(p, tournament)
        }
        container.add(p)
    }

    private fun createCard(container: Container, tournament: Tournament) {
        val m = Moment(tournament.date)
        val dateStr = m.format("ddd, MMM Do") as String
        val timeStr = m.format("h:mm A z") as String

        container.add(
            FlexPanel(FlexDir.ROW) {
                div {
                    addCssClass(Styles.cardStyle.main)
                    flexPanel(direction = FlexDir.COLUMN) {
                        addCssClass(Styles.cardStyle.panel)
                        div {
                            // the header image
                            addCssClass(Styles.cardStyle.headerImageContainer)
                            val i = image(
                                src = tournament.cardImage,
                                alt = tournament.name,
                                classes = setOf("custom-cardstyle-header-image")
                            )
                            this.add(i)
                        }
                        div {
                            // the details
                            addCssClass(Styles.cardStyle.info.block)
                            div(classes = setOf("custom-cardstyle-info-gamename")) {
                                addCssClass(Styles.cardStyle.info.gameName)
                                // The game name
                                content = "Elder Scrolls Legends"
                            }
                            div {
                                addCssClass(Styles.cardStyle.info.title)
                                // The Title
                                content = tournament.name
                            }

                            // some hr element
                            tag(TAG.HR) {
                                addCssClass(Styles.cardStyle.info.hr)

                            }

                            // Date/Time Tables
                            flexPanel(
                                direction = FlexDir.ROW,
                                wrap = FlexWrap.NOWRAP,
                                justify = FlexJustify.FLEXSTART,
                                alignItems = FlexAlignItems.CENTER,
                                alignContent = FlexAlignContent.STRETCH,
                                spacing = 0
                            ) {
                                addCssClass(Styles.cardStyle.info.details)
                                add(child = createTable("DATE", dateStr, "TIME", timeStr), grow = 1)
                                add(child = createTable("REGION", "Everywhere!", "TYPE", "Abell Wild Tournament"), grow = 1)
                            }

                            // Rules
                        }
                    }
                }
            }
        )
    }

    private fun createTable(heading1: String, data1: String, heading2: String, data2: String) : Table {
        return Table {
            addCssClass(Styles.cardStyle.info.table)
            row {
                addCssClass(Styles.cardStyle.info.row)
                cell {
                    addCssClass(Styles.cardStyle.info.cell)
                    span(content = heading1)
                }
                cell(content = data1) {
                    addCssClass(Styles.cardStyle.info.cell)
                }
            }
            row {
                addCssClass(Styles.cardStyle.info.row)
                cell {
                    addCssClass(Styles.cardStyle.info.cell)
                    span(content = heading2)
                }
                cell(content = data2) {
                    addCssClass(Styles.cardStyle.info.cell)
                }
            }
        }
    }

    override fun dispose(): Map<String, Any> {
        root.dispose()
        return mapOf()
    }

    val css = require("./css/tournament.css")

    private fun downloadTournaments(): ActionCreator<dynamic, TournamentState> {
        return { dispatch, _ ->
            val restClient = RestClient()
            dispatch(TournamentAction.StartDownload)
            restClient.remoteCall(
                url = "http://localhost:8080/tournament",
                deserializer = Tournament.serializer().list
            ) {
                it
            }.then { list ->
                console.log(list)
                dispatch(TournamentAction.DownloadOK)
                dispatch(TournamentAction.SetTournamentList(list))
                dispatch(TournamentAction.SetSearchString(null))
            }.catch { e ->
                val info = if (!e.message.isNullOrBlank()) {
                    " (${e.message})"
                } else {
                    "<no message>"
                }
                console.log("ERROR: $info\n$e")
                dispatch(TournamentAction.DownloadError("Service error! $info"))
            }
        }
    }
}
