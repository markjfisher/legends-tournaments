package tournament.api

import kotlinx.serialization.Serializable
import kotlinx.serialization.UnstableDefault
import network.getBase

class Stuff {

    @UnstableDefault
    suspend fun getThing() {
        val x = getBase("http://localhost:8000/getThing.json", Thing::class, Thing.serializer())
        println("got: $x")

    }

}

@Serializable
data class Thing(
    val name: String
)

@Serializable
data class ThingResponse(
    val foo: String
)