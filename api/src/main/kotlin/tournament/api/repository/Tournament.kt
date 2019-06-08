package tournament.api.repository

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.serialization.ContextualSerialization
import kotlinx.serialization.Serializable
import tournament.api.DateSerializer
import java.time.Instant
import java.util.*

@Serializable
data class Tournament(
    val id: String = "",
    val name: String = "",
    @Serializable(with = DateSerializer::class)
    val date: Date = Date.from(Instant.EPOCH),
    val rules: List<String> = emptyList()

) {
    @ContextualSerialization
    private val mapper = jacksonObjectMapper()
        .registerModule(JavaTimeModule())
        .apply { setSerializationInclusion(JsonInclude.Include.NON_NULL) }

    fun asJson(): String = mapper.writeValueAsString(this)

    fun withId(uuid: String): Tournament {
        return Tournament(id = uuid, name = name, date = date, rules = rules)
    }
}