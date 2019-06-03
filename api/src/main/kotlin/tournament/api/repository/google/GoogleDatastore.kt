package tournament.api.repository.google

import com.google.cloud.datastore.Datastore
import com.google.cloud.datastore.DatastoreOptions
import com.google.cloud.datastore.KeyFactory
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Requires
import mu.KotlinLogging
import tournament.api.Configuration
import javax.inject.Named
import javax.inject.Singleton

private val logger = KotlinLogging.logger {}

@Factory
@Requires(notEnv = ["test"])
class GoogleDatastore(
        private val configuration: Configuration
) {

    @Singleton
    fun createDatastore(): Datastore {
        logger.info { "creating datastore with \n - datastore.projectId = ${configuration.datastore.projectid}\n - datastore.namespace = ${configuration.datastore.namespace}" }

        return DatastoreOptions
                .newBuilder()
                .setProjectId(configuration.datastore.projectid)
                .setNamespace(configuration.datastore.namespace)
                .build()
                .service
    }

}

@Factory
class KeyFactoryConfig(private val datastore: Datastore) {
    @Singleton
    @Bean
    @Named("Tournament")
    fun createTournamentKeyFactory() : KeyFactory = datastore.newKeyFactory().setKind(DatastoreRepository.TOURNAMENT_KIND)
}