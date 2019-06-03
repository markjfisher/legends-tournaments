package tournament.api.repository.google

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.uuid.Generators
import com.google.cloud.Timestamp
import com.google.cloud.datastore.*
import io.micronaut.context.annotation.Requires
import tournament.api.repository.*
import tournament.api.repository.google.DatastoreRepository.Companion.TOURNAMENT_JSON_PROPERTY
import java.time.Instant
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Singleton

@Singleton
@Requires(env = ["cloud"], notEnv = ["test"])
open class DatastoreRepository(
    private val datastore: Datastore
) : TournamentRepository {
    private val keyFactories = ConcurrentHashMap<String, KeyFactory>()

    companion object {
        const val TOURNAMENT_ID_PROPERTY: String = "id"
        const val TOURNAMENT_JSON_PROPERTY: String = "json"
        const val TOURNAMENT_NAME_PROPERTY: String = "name"
        const val TOURNAMENT_DATE_PROPERTY: String = "date"
        const val TOURNAMENT_KIND = "Tournament"
    }

    override fun getTournaments(): List<Tournament> {
        return fetchTournaments()
    }

    override fun getTournamentById(id: String): Tournament? {
        return fetchTournaments(id, this::matchOnTournamentId).firstOrNull()
    }

    override fun getTournamentByName(name: String): Tournament? {
        return fetchTournaments(name, this::matchOnTournamentName).firstOrNull()
    }

    private fun fetchTournaments(
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

    override fun saveTournament(tournament: Tournament): ServiceResult {
        val tournamentByName = getTournamentByName(tournament.name)
        if (tournamentByName != null) {
            return ServiceResult(
                    message = "Found existing tournament with name ${tournament.name}",
                    serviceStatus = ServiceStatus.CONFLICT,
                    tournament = tournament
            )
        }

        var transaction: Transaction? = null
        try {
            val tournamentWithUUID = tournament.withId(createId())

            transaction = datastore.newTransaction()
            transaction.put(createEntity(tournamentWithUUID))
            transaction.commit()
            return ServiceResult(message = "", serviceStatus = ServiceStatus.CREATED, tournament = tournamentWithUUID)
        } catch (e: Exception) {
            throw RepositoryException(cause = e)
        } finally {
            if (transaction != null && transaction.isActive) {
                transaction.rollback()
            }
        }
    }

    override fun updateTournament(tournament: Tournament): ServiceResult {
        getTournamentById(tournament.id) ?: return saveTournament(tournament)

        // update it
        var transaction: Transaction? = null
        try {
            transaction = datastore.newTransaction()
            transaction.put(createEntity(tournament))
            transaction.commit()

            // PUT returns either OK or NO_CONTENT
            return ServiceResult(message = "", serviceStatus = ServiceStatus.NO_CONTENT, tournament = tournament)
        } catch (e: Exception) {
            throw RepositoryException(cause = e)
        } finally {
            if (transaction != null && transaction.isActive) {
                transaction.rollback()
            }
        }
    }

    override fun deleteTournament(id: String): ServiceStatus {
        if (getTournamentById(id) == null) {
            return ServiceStatus.NOT_FOUND
        }

        var transaction: Transaction? = null
        try {
            transaction = datastore.newTransaction()
            transaction.delete(getKey(id,
                TOURNAMENT_KIND
            ))
            transaction.commit()
            return ServiceStatus.ACCEPTED
        } catch (e: Exception) {
            throw RepositoryException(cause = e)
        } finally {
            if (transaction != null && transaction.isActive) {
                transaction.rollback()
            }
        }
    }

    private fun matchOnTournamentId(id: String) = StructuredQuery.PropertyFilter.eq(TOURNAMENT_ID_PROPERTY, id)
    private fun matchOnTournamentName(name: String) = StructuredQuery.PropertyFilter.eq(TOURNAMENT_NAME_PROPERTY, name)

    private fun createEntity(tournament: Tournament): Entity {
        return Entity
            .newBuilder(getKey(tournament.id,
                TOURNAMENT_KIND
            ))
            .set(TOURNAMENT_ID_PROPERTY, tournament.id)
            .set(TOURNAMENT_NAME_PROPERTY, tournament.name)
            .set(TOURNAMENT_DATE_PROPERTY, Timestamp.of(Date.from(tournament.date)))
            .set(TOURNAMENT_JSON_PROPERTY, StringValue.newBuilder(tournament.asJson())
                    .setExcludeFromIndexes(true)
                    .build())
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

private val mapper = jacksonObjectMapper()
    .registerModule(JavaTimeModule())
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

fun Entity.convertToTournament() = mapper.readValue<Tournament>(getString(TOURNAMENT_JSON_PROPERTY))
