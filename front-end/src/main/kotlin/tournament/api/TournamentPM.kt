package tournament.api

// PM = Presentation Model
data class TournamentPM(val text: String, var isDone: Boolean = false) {
    val id: Int = nextId

    companion object {
        private var nextId = 0
            get() = field++
    }
}