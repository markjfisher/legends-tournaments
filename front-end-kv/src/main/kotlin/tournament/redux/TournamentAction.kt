package tournament.redux

import redux.RAction
import tournament.model.Tournament

sealed class TournamentAction: RAction {
    object StartDownload: TournamentAction()
    object DownloadOK: TournamentAction()
    data class DownloadError(val errorMessage: String): TournamentAction()
    data class SetTournamentList(val tournaments: List<Tournament>): TournamentAction()
    data class SetSearchString(val searchString: String?): TournamentAction()
    object HomePage: TournamentAction()
    object ViewPage: TournamentAction()
    object FormPage: TournamentAction()
}