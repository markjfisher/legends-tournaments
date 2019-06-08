package tournament

import kotlinx.serialization.list
import pl.treksoft.kvision.core.Background
import pl.treksoft.kvision.core.BgRepeat
import pl.treksoft.kvision.core.BgSize
import pl.treksoft.kvision.core.Container
import pl.treksoft.kvision.hmr.ApplicationBase
import pl.treksoft.kvision.html.Div
import pl.treksoft.kvision.html.Div.Companion.div
import pl.treksoft.kvision.html.H1.Companion.h1
import pl.treksoft.kvision.html.Link.Companion.link
import pl.treksoft.kvision.html.P.Companion.p
import pl.treksoft.kvision.html.TAG
import pl.treksoft.kvision.html.Tag.Companion.tag
import pl.treksoft.kvision.i18n.DefaultI18nManager
import pl.treksoft.kvision.i18n.I18n
import pl.treksoft.kvision.navbar.Nav.Companion.nav
import pl.treksoft.kvision.navbar.NavForm.Companion.navForm
import pl.treksoft.kvision.navbar.Navbar.Companion.navbar
import pl.treksoft.kvision.panel.FlexJustify
import pl.treksoft.kvision.panel.HPanel
import pl.treksoft.kvision.panel.HPanel.Companion.hPanel
import pl.treksoft.kvision.panel.Root
import pl.treksoft.kvision.panel.VPanel
import pl.treksoft.kvision.panel.VPanel.Companion.vPanel
import pl.treksoft.kvision.redux.ActionCreator
import pl.treksoft.kvision.redux.StateBinding.Companion.stateBinding
import pl.treksoft.kvision.redux.createReduxStore
import pl.treksoft.kvision.require
import pl.treksoft.kvision.rest.RestClient
import pl.treksoft.kvision.utils.pc
import pl.treksoft.kvision.utils.px
import tournament.html.Panel.Companion.createPanel
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
            background = Background(
                image = "/images/mech-dragon.jpg",
                size = BgSize.CONTAIN,
                repeat = BgRepeat.NOREPEAT
            )
            height = 100.pc

            vPanel(justify = FlexJustify.CENTER, spacing = 0) {
                navbar("") {
                    marginBottom = 5.px
                    nav {
                        tag(TAG.LI) {
                            link("Tournaments", url = "#!/")
                        }
                        tag(TAG.LI) {
                            link("View", icon = "fa-trophy", url = "#!/view")
                        }
                        navForm(rightAlign = true) {
                        }
                    }
                }


                hPanel(justify = FlexJustify.CENTER) {
                    div(classes = setOf("jumbotron")) {
                        h1(content = "Legends Tournaments Home")
                        p(content = "Welcome to the Legends Tournaments Site")
                    }
                }.stateBinding(store) { state ->
                    when (state.viewMode) {
                        VIEWMODE.HOME -> showHome(this, state)
                        VIEWMODE.VIEW -> showView(this, state)
                    }
                }


            }

            Routing(hash = "#!")
                .on( location = "/", handler = { _ ->
                    store.dispatch(TournamentAction.HomePage)
                    console.log("dispatched home page")
                })
                .on("/view", { _ ->
                    store.dispatch(TournamentAction.ViewPage)
                    console.log("dispatched view page")
                })
                .resolve()
        }

        store.dispatch(downloadTournaments())
        store.dispatch(TournamentAction.HomePage)
    }

    private fun showHome(container: Container, state: TournamentState) {
        val p = VPanel {
            div(classes = setOf("jumbotron")) {
                h1(content = "Legends Tournaments Home")
                p(content = "Welcome to the Legends Tournaments Site")
            }
        }
        container.add(p)
    }

    private fun showView(container: Container, state: TournamentState) {
        val p = HPanel()
        state.tournaments.forEach { tournament ->
            val d = Div {
                createPanel(title = tournament.name, content = tournament.toString())
            }
            p.add(d)
        }
        container.add(p)
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
