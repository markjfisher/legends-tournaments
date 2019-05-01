package tournament.api.repository

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.uuid.Generators
import com.google.cloud.Timestamp
import com.google.cloud.datastore.*
import io.micronaut.http.HttpStatus
import mu.KotlinLogging
import tournament.api.repository.TournamentEntityRepository.Companion.TOURNAMENT_JSON_PROPERTY
import java.time.Instant
import java.util.*
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
        const val TOURNAMENT_JSON_PROPERTY: String = "json"
        const val TOURNAMENT_NAME_PROPERTY: String = "name"
        const val TOURNAMENT_DATE_PROPERTY: String = "date"
        const val TOURNAMENT_KIND = "Tournament"
    }

    fun getAllTournaments(): List<Tournament> {
        return getTournaments()
    }

    fun getTournamentById(id: String): Tournament? {
        return getTournaments(id, this::matchOnTournamentId).firstOrNull()
    }

    fun getTournamentByName(name: String): Tournament? {
        return getTournaments(name, this::matchOnTournamentName).firstOrNull()
    }

    private fun getTournaments(
        queryValue: String = "",
        matcher: (v: String) -> StructuredQuery.PropertyFilter? = { null }
    ): List<Tournament> {
        var queryBuilder = Query.newEntityQueryBuilder()
            .setKind("Tournament")

        if (queryValue.isNotEmpty()) {
            queryBuilder = queryBuilder.setFilter(matcher(queryValue))
        }

        val query = queryBuilder.build()

        return datastore.run(query)
            .asSequence()
            .map { it.convertToTournament() }
            .toList()
    }

    private fun matchOnTournamentId(id: String) = StructuredQuery.PropertyFilter.eq(TOURNAMENT_ID_PROPERTY, id)
    private fun matchOnTournamentName(name: String) = StructuredQuery.PropertyFilter.eq(TOURNAMENT_NAME_PROPERTY, name)

    open fun saveTournament(tournament: Tournament): Pair<ReturnStatus, Tournament> {
        val tournamentByName = getTournamentByName(tournament.name)
        if (tournamentByName != null) {
            return Pair(
                ReturnStatus(
                    message = "Found existing tournament with name ${tournament.name}",
                    httpStatus = HttpStatus.CONFLICT
                ),
                tournament
            )
        }

        var transaction: Transaction? = null
        try {
            val tournamentWithUUID = tournament.withId(createId())

            transaction = datastore.newTransaction()
            transaction.put(createEntity(tournamentWithUUID))
            transaction.commit()
            return Pair(ReturnStatus(message = "", httpStatus = HttpStatus.CREATED), tournamentWithUUID)
        } catch (e: Exception) {
            throw RepositoryException(cause = e)
        } finally {
            if (transaction != null && transaction.isActive) {
                transaction.rollback()
            }
        }
    }

    open fun updateTournament(tournament: Tournament): Pair<ReturnStatus, Tournament> {
        getTournamentById(tournament.id) ?: return saveTournament(tournament)

        // update it
        var transaction: Transaction? = null
        try {
            transaction = datastore.newTransaction()
            transaction.put(createEntity(tournament))
            transaction.commit()

            // PUT returns either OK or NO_CONTENT
            return Pair(ReturnStatus(message = "", httpStatus = HttpStatus.NO_CONTENT), tournament)
        } catch (e: Exception) {
            throw RepositoryException(cause = e)
        } finally {
            if (transaction != null && transaction.isActive) {
                transaction.rollback()
            }
        }
    }

    open fun deleteTournament(id: String): HttpStatus {
        if (getTournamentById(id) == null) {
            return HttpStatus.NOT_FOUND
        }

        var transaction: Transaction? = null
        try {
            transaction = datastore.newTransaction()
            transaction.delete(getKey(id, TOURNAMENT_KIND))
            transaction.commit()
            return HttpStatus.ACCEPTED
        } catch (e: Exception) {
            throw RepositoryException(cause = e)
        } finally {
            if (transaction != null && transaction.isActive) {
                transaction.rollback()
            }
        }
    }


    private fun createEntity(tournament: Tournament): Entity {
        return Entity
            .newBuilder(getKey(tournament.id, TOURNAMENT_KIND))
            .set(TOURNAMENT_ID_PROPERTY, tournament.id)
            .set(TOURNAMENT_NAME_PROPERTY, tournament.name)
            .set(TOURNAMENT_DATE_PROPERTY, Timestamp.of(Date.from(tournament.date)))
            .set(
                TOURNAMENT_JSON_PROPERTY, StringValue.newBuilder(tournament.asJson())
                    .setExcludeFromIndexes(true)
                    .build()
            )
            .build()
    }

    private fun getKey(id: String, kind: String): Key {
        return keyFactories.getOrPut(kind) { datastore.newKeyFactory().setKind(kind) }.newKey(id)
    }

    private fun createId() = Generators.randomBasedGenerator().generate().toString()

    fun getTournamentDate(entity: Entity): Instant {
        val datastoreTimestamp = entity.getValue<TimestampValue>(TOURNAMENT_DATE_PROPERTY).get()
        return datastoreTimestamp.toDate().toInstant()
    }


}

data class ReturnStatus(
    val message: String,
    val httpStatus: HttpStatus
)

private val mapper = jacksonObjectMapper()
    .registerModule(JavaTimeModule())
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

fun Entity.convertToTournament() = mapper.readValue<Tournament>(getString(TOURNAMENT_JSON_PROPERTY))
