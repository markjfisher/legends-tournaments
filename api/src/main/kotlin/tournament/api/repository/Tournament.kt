package tournament.api.repository

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.time.Instant

data class Tournament(
    val id: String = "",
    val name: String = "",
    val date: Instant = Instant.EPOCH,
    val rules: List<String> = emptyList()

) {
    private val mapper = jacksonObjectMapper()
        .registerModule(JavaTimeModule())
        .apply { setSerializationInclusion(JsonInclude.Include.NON_NULL) }

    fun asJson(): String = mapper.writeValueAsString(this)

    fun withId(uuid: String): Tournament {
        return Tournament(id = uuid, name = name, date = date, rules = rules)
    }
}