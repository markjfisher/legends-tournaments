package tournament.redux

import tournament.model.Tournament
import tournament.model.TournamentState
import tournament.model.VIEWMODE

fun tournamentStateReducer(state: TournamentState, action: TournamentAction): TournamentState = when (action) {
    is TournamentAction.HomePage -> state.copy(viewMode = VIEWMODE.HOME)
    is TournamentAction.ViewPage -> state.copy(viewMode = VIEWMODE.VIEW)

    is TournamentAction.StartDownload -> state.copy(downloading = true)
    is TournamentAction.DownloadOK -> {
        state.copy(downloading = false)
    }
    is TournamentAction.DownloadError -> state.copy(downloading = false, errorMessage = action.errorMessage)
    is TournamentAction.SetTournamentList -> {
        state.copy(tournaments = action.tournaments)
    }
    is TournamentAction.SetSearchString -> {
        val filteredTournament = state.tournaments.filterBySearchString(action.searchString)
        state.copy(
            visibleTournaments = filteredTournament,
            searchString = action.searchString
        )
    }
}

fun List<Tournament>.filterBySearchString(searchString: String?): List<Tournament> {
    return searchString?.let { search ->
        this.filter {
            it.name.toLowerCase().contains(search.toLowerCase())
        }
    } ?: this
}
