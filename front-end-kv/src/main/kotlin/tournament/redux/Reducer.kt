package tournament.redux

import tournament.model.Tournament
import tournament.model.TournamentState
import tournament.model.VIEWMODE

fun tournamentStateReducer(state: TournamentState, action: TournamentAction): TournamentState = when (action) {
    is TournamentAction.HomePage -> state.copy(viewMode = VIEWMODE.HOME)
    is TournamentAction.ViewPage -> state.copy(viewMode = VIEWMODE.LIST_TOURNAMENTS)
    is TournamentAction.FormPage -> state.copy(viewMode = VIEWMODE.FORM)
    is TournamentAction.EditTournamentPage -> {
        state.copy(
            viewMode = VIEWMODE.TOURNAMENT_EDIT,
            editTournament = state.tournaments.firstOrNull { it.id == action.tournamentId }
        )
    }

    is TournamentAction.StartDownload -> {
        console.log("r: started download")
        state.copy(downloading = true)
    }
    is TournamentAction.DownloadOK -> {
        console.log("r: download was ok")
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
