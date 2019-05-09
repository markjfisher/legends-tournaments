package tournament.api

import com.narbase.kunafa.core.lifecycle.Observable

class TournamentViewModel {
    val onItemAdded = Observable<TournamentPM>()
    val onItemDeleted = Observable<TournamentPM>()
    val onItemUpdated = Observable<TournamentPM>()

    private val tournamentItemsList = mutableListOf<TournamentPM>()

    fun addNewTournament(tournamentText: String?) {
        if (tournamentText.isNullOrBlank()) return
        val pm = TournamentPM(tournamentText)
        tournamentItemsList.add(pm)
        onItemAdded.value = pm
    }

    fun deleteItem(id: Int) {
        val item = tournamentItemsList.find { it.id == id } ?: return
        onItemDeleted.value = item
        tournamentItemsList.remove(item)
    }

    fun toggleItemStatus(id: Int) {
        val item = tournamentItemsList.find { it.id == id } ?: return
        item.isDone = item.isDone.not()
        onItemUpdated.value = item
    }
}