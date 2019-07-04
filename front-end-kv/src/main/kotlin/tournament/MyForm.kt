package tournament

import kotlinx.serialization.Serializable
import pl.treksoft.kvision.types.KFile
import kotlin.js.Date

@Serializable
data class MyForm(
    val text: String? = null,
    val password: String? = null,
    val password2: String? = null,
    val textarea: String? = null,
    val richtext: String? = null,
    @Serializable(with = DateSerializer::class)
    val date: Date? = null,
    @Serializable(with = DateSerializer::class)
    val time: Date? = null,
    val checkbox: Boolean = false,
    val radio: Boolean = false,
    val select: String? = null,
    val ajaxselect: String? = null,
    val spinner: Int? = null,
    val radiogroup: String? = null,
    val upload: List<KFile>? = null
)