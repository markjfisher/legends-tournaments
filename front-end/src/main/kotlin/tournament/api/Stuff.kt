package tournament.api

import kotlinx.serialization.Serializable
import kotlinx.serialization.UnstableDefault
import network.getBase
import network.postBase
import network.putBase

class Stuff {

    @UnstableDefault
    suspend fun getThing() {
        val x = getBase("http://localhost:8000/getThing.json", ThingResponse::class, ThingResponse.serializer(), true)
        println("got: $x")

    }

    @UnstableDefault
    suspend fun postThing(thing: Thing): ThingResponse {
        return postBase("http://localhost:8000/new.json", thing, Thing.serializer(), ThingResponse.serializer(), true)
    }

    @UnstableDefault
    suspend fun putThing(thing: Thing): ThingResponse {
        return putBase("http://localhost:8000/new.json", thing, Thing.serializer(), ThingResponse.serializer(), true)
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