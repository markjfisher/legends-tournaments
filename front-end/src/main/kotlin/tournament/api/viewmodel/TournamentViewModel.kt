package tournament.api.viewmodel

import com.narbase.kunafa.core.lifecycle.Observable
import tournament.api.model.TournamentModel

class TournamentViewModel {
    val onItemAdded = Observable<TournamentModel>()
    val onItemDeleted = Observable<TournamentModel>()
    val onItemUpdated = Observable<TournamentModel>()

    private val tournamentItemsList = mutableListOf<TournamentModel>()

    fun addNewTournament(tournamentText: String?) {
        if (tournamentText.isNullOrBlank()) return
        val pm = TournamentModel(tournamentText)
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