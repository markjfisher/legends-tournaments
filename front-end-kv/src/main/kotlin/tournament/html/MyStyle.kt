package tournament.html

import pl.treksoft.kvision.core.Style

class MyStyle {
    companion object {
        fun Style.mystyle(className: String? = null, init: (Style.() -> Unit)? = null): Style {
            return Style(className, this, init)
        }
    }
}