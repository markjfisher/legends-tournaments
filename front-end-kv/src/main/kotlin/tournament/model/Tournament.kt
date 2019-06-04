package tournament.model

import kotlinx.serialization.Serializable

@Serializable
data class Tournament(
    val id: String,
    val name: String,
    val date: String?,
    val rules: List<String> = emptyList()
)

@Serializable
data class TournamentState(
    val tournaments: List<Tournament>,
    val downloading: Boolean,
    val errorMessage: String?,
    val visibleTournaments: List<Tournament>,
    val searchString: String?,
    val pageNumber: Int,
    val numberOfPages: Int
) {
    companion object {
        const val MAX_ON_PAGE = 12
    }
}
