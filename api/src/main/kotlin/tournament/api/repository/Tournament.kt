package tournament.api.repository

data class Tournament(
    val id: String,
    val name: String = "",
    val date: String = "",
    val rules: List<String> = emptyList()
)
