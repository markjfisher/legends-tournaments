package tournament.model

import kotlinx.serialization.Serializable
import tournament.DateSerializer
import kotlin.js.Date

@Serializable
data class Tournament(
    val id: String,
    val name: String,
    val cardImage: String,
    @Serializable(with = DateSerializer::class)
    val date: Date,
    val rules: List<String> = emptyList()
)

@Serializable
data class TournamentState(
    val viewMode: VIEWMODE = VIEWMODE.HOME,
    val tournaments: List<Tournament>,
    val downloading: Boolean,
    val errorMessage: String?,
    val visibleTournaments: List<Tournament>,
    val searchString: String?,
    val editTournament: Tournament?
)

enum class VIEWMODE {
    HOME, LIST_TOURNAMENTS, FORM, TOURNAMENT_EDIT;
}
