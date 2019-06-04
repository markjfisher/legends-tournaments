package tournament.redux

import tournament.model.Tournament
import tournament.model.TournamentState
import tournament.model.TournamentState.Companion.MAX_ON_PAGE

fun tournamentStateReducer(state: TournamentState, action: TournamentAction): TournamentState = when (action) {
    is TournamentAction.StartDownload -> state.copy(downloading = true)
    is TournamentAction.DownloadOK -> state.copy(downloading = false)
    is TournamentAction.DownloadError -> state.copy(downloading = false, errorMessage = action.errorMessage)
    is TournamentAction.SetTournamentList -> state.copy(tournaments = action.tournaments)
    is TournamentAction.SetSearchString -> {
        val filteredTournament = state.tournaments.filterBySearchString(action.searchString)
        val visibleTournaments = filteredTournament.take(MAX_ON_PAGE)
        state.copy(
            visibleTournaments = visibleTournaments,
            searchString = action.searchString,
            pageNumber = 0,
            numberOfPages = ((filteredTournament.size - 1) / MAX_ON_PAGE) + 1
        )
    }
    is TournamentAction.NextPage -> if (state.pageNumber < state.numberOfPages - 1) {
        val newPageNumber = state.pageNumber + 1
        val visibleTournaments =
            state.tournaments.filterBySearchString(state.searchString).subListByPageNumber(newPageNumber)
        state.copy(tournaments = visibleTournaments, pageNumber = newPageNumber)
    } else {
        state
    }
    is TournamentAction.PreviousPage -> if (state.pageNumber > 0) {
        val newPageNumber = state.pageNumber - 1
        val visibleTournaments =
            state.tournaments.filterBySearchString(state.searchString).subListByPageNumber(newPageNumber)
        state.copy(visibleTournaments = visibleTournaments, pageNumber = newPageNumber)
    } else {
        state
    }
}

fun List<Tournament>.filterBySearchString(searchString: String?): List<Tournament> {
    return searchString?.let { search ->
        this.filter {
            it.name.toLowerCase().contains(search.toLowerCase())
        }
    } ?: this
}

fun List<Tournament>.subListByPageNumber(pageNumber: Int): List<Tournament> {
    return this.subList((pageNumber) * MAX_ON_PAGE, (pageNumber + 1) * MAX_ON_PAGE)
}