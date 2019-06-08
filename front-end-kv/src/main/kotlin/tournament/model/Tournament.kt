package tournament.model

import kotlinx.serialization.Serializable
import tournament.DateSerializer
import kotlin.js.Date

@Serializable
data class Tournament(
    val id: String,
    val name: String,
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
    val searchString: String?
)

enum class VIEWMODE {
    HOME, VIEW;
}
