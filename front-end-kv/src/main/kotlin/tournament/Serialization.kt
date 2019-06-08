package tournament

import kotlinx.serialization.*
import kotlinx.serialization.internal.StringDescriptor
import kotlin.js.Date

@Serializer(forClass = Date::class)
object DateSerializer: KSerializer<Date> {
    override val descriptor: SerialDescriptor = StringDescriptor.withName("WithCustomDefault")

    override fun serialize(encoder: Encoder, obj: Date) {
        encoder.encodeDouble(obj.getTime())
    }

    override fun deserialize(decoder: Decoder): Date {
        return Date(decoder.decodeDouble())
    }
}