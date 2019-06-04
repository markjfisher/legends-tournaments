package tournament

import kotlinx.serialization.list
import pl.treksoft.kvision.core.Background
import pl.treksoft.kvision.core.BgRepeat
import pl.treksoft.kvision.core.BgSize
import pl.treksoft.kvision.hmr.ApplicationBase
import pl.treksoft.kvision.html.Div.Companion.div
import pl.treksoft.kvision.html.H1.Companion.h1
import pl.treksoft.kvision.html.Link.Companion.link
import pl.treksoft.kvision.html.P.Companion.p
import pl.treksoft.kvision.html.TAG
import pl.treksoft.kvision.html.Tag.Companion.tag
import pl.treksoft.kvision.i18n.DefaultI18nManager
import pl.treksoft.kvision.i18n.I18n
import pl.treksoft.kvision.i18n.I18n.gettext
import pl.treksoft.kvision.navbar.Nav.Companion.nav
import pl.treksoft.kvision.navbar.NavForm.Companion.navForm
import pl.treksoft.kvision.navbar.Navbar.Companion.navbar
import pl.treksoft.kvision.panel.FlexJustify
import pl.treksoft.kvision.panel.Root
import pl.treksoft.kvision.panel.VPanel.Companion.vPanel
import pl.treksoft.kvision.redux.ActionCreator
import pl.treksoft.kvision.redux.createReduxStore
import pl.treksoft.kvision.require
import pl.treksoft.kvision.rest.RestClient
import pl.treksoft.kvision.utils.obj
import pl.treksoft.kvision.utils.pc
import tournament.model.Tournament
import tournament.model.TournamentState
import tournament.redux.TournamentAction
import tournament.redux.tournamentStateReducer

object TournamentApp : ApplicationBase {
    private lateinit var root: Root

    private val store = createReduxStore(
        ::tournamentStateReducer,
        TournamentState(
            tournaments = emptyList(),
            downloading = false,
            visibleTournaments = emptyList(),
            pageNumber = 0,
            numberOfPages = 1,
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
                navbar("Tournaments") {
                    nav {
                        tag(TAG.LI) {
                            link("View", icon = "fa-trophy", url = "#!/view")
                        }
                        navForm(rightAlign = true) {
                        }
                    }
                }

                div(classes = setOf("jumbotron")) {
                    h1(content = "Legends Tournaments Home")
                    p(content = "Welcome to the Legends Tournaments Site")
                }
            }
        }

        // store.dispatch(downloadTournaments())
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
                "http://localhost:8080/tournament", obj { limit = 800 },
                deserializer = Tournament.serializer().list
            ) {
                it.results
            }.then { list ->
                dispatch(TournamentAction.DownloadOK)
                dispatch(TournamentAction.SetTournamentList(list))
                dispatch(TournamentAction.SetSearchString(null))
            }.catch { e ->
                val info = if (!e.message.isNullOrBlank()) {
                    " (${e.message})"
                } else {
                    ""
                }
                dispatch(TournamentAction.DownloadError(gettext("Service error!") + info))
            }
        }
    }
}
