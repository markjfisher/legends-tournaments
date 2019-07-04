package tournament.html

import com.github.snabbdom.VNode
import org.w3c.dom.Document
import pl.treksoft.kvision.core.Container
import pl.treksoft.kvision.html.Div
import kotlin.dom.createElement

data class HR(
    val classes: Set<String> = setOf()
) : Div() {
    override fun afterInsert(node: VNode) {
        val div = this.getElement()
        val hr = Document().createElement(name = "hr") {
            if (classes.isNotEmpty()) {
                setAttribute("class", classes.joinToString(separator = " "))
            }
        }
        div?.parentNode?.replaceChild(hr, div)
    }

    companion object {
        fun Container.hr(
            classes: Set<String> = setOf()
        ): HR {
            val hr = HR(classes = classes)
            this.add(hr)
            return hr
        }

    }
}