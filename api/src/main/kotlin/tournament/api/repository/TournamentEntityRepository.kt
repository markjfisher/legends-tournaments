package tournament.api.repository

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.cloud.datastore.Datastore
import com.google.cloud.datastore.Entity
import com.google.cloud.datastore.Query
import com.google.cloud.datastore.StructuredQuery
import mu.KotlinLogging
import tournament.api.repository.TournamentEntityRepository.Companion.JSON_PROPERTY
import javax.inject.Singleton

private val logger = KotlinLogging.logger {}

@Singleton
open class TournamentEntityRepository(
    private val datastore: Datastore
) {
    companion object {
        const val TOURNAMENT_ID_PROPERTY: String = "id"
        const val JSON_PROPERTY: String = "json"
    }

    fun getAllTournaments(): List<Tournament> {
        return getTournaments("")
    }

    fun getTournamentById(id: String): Tournament? {
        return getTournaments(id).firstOrNull()
    }

    private fun getTournaments(id: String): List<Tournament> {
        logger.info { "getting tournament by id $id" }
        var queryBuilder = Query.newEntityQueryBuilder()
            .setKind("Tournament")

        if (id.isNotEmpty()) {
            queryBuilder = queryBuilder.setFilter(matchOnTournamentId(id))
        }

        val query = queryBuilder.build()

        return datastore.run(query)
            .asSequence()
            .map { it.convertToTournament() }
            .toList()
    }

    private fun matchOnTournamentId(id: String) = StructuredQuery.PropertyFilter.eq(TOURNAMENT_ID_PROPERTY, id)
}

private val mapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

fun Entity.convertToTournament() = mapper.readValue<Tournament>(getString(JSON_PROPERTY))
