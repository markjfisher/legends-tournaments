package tournament.api.repository

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.cloud.datastore.*
import mu.KotlinLogging
import tournament.api.repository.TournamentEntityRepository.Companion.JSON_PROPERTY
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Singleton

private val logger = KotlinLogging.logger {}

@Singleton
open class TournamentEntityRepository(
    private val datastore: Datastore
) {
    private val keyFactories = ConcurrentHashMap<String, KeyFactory>()

    companion object {
        const val TOURNAMENT_ID_PROPERTY: String = "id"
        const val JSON_PROPERTY: String = "json"
        const val TOURNAMENT_KIND = "Tournament"
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

    open fun saveTournament(tournament: Tournament): Tournament {
        var transaction: Transaction? = null
        try {
            transaction = datastore.newTransaction()

            val listValueBuilder = ListValue.newBuilder()
            tournament.rules.forEach { listValueBuilder.addValue(it) }
            val rules = listValueBuilder.build()

            val tournamentEntity = Entity
                .newBuilder(getKey(tournament.id, TOURNAMENT_KIND))
                .set(JSON_PROPERTY, StringValue.newBuilder(tournament.asJson())
                    .setExcludeFromIndexes(true)
                    .build())
                .set(TOURNAMENT_ID_PROPERTY, tournament.id)
                .build()

            transaction.put(tournamentEntity)

            transaction.commit()
        } catch (e: Exception) {
            throw RepositoryException(cause = e)
        } finally {
            if (transaction != null && transaction.isActive) {
                transaction.rollback()
            }
        }
        return tournament
    }

    private fun getKey(id: String, kind: String): Key {
        return keyFactories.getOrPut(kind) { datastore.newKeyFactory().setKind(kind) }.newKey(id)
    }

}

private val mapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
fun Entity.convertToTournament() = mapper.readValue<Tournament>(getString(JSON_PROPERTY))
