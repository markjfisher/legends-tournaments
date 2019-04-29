package tournament.api

import io.micronaut.context.annotation.ConfigurationProperties
import javax.inject.Inject

@ConfigurationProperties("tournament")
class Configuration {

    @Inject
    internal var datastore = Datastore()

    @ConfigurationProperties("datastore")
    internal class Datastore {
        lateinit var projectid: String
        lateinit var namespace: String
    }

    @Inject
    internal var security = Security()

    @ConfigurationProperties("security")
    internal class Security {

        @Inject
        internal var endpoints = Endpoints()

        @ConfigurationProperties("endpoints")
        internal class Endpoints {
            lateinit var username: String
            lateinit var password: String
        }
    }
}