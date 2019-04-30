package tournament.api.repository

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import javax.validation.constraints.NotEmpty

data class Tournament(
    val id: String,

    @NotEmpty(message = "A tournament must have a name")
    val name: String = "",

    @NotEmpty(message = "A tournament must have a date")
    val date: String = "",

    val rules: List<String> = emptyList()

) {
    private val mapper = jacksonObjectMapper()
        .registerModule(JavaTimeModule())
        .apply { setSerializationInclusion(JsonInclude.Include.NON_NULL) }

    fun asJson(): String = mapper.writeValueAsString(this)
}