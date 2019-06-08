package tournament.html

import pl.treksoft.navigo.Navigo

open class Routing(root: String? = null, useHash: Boolean = true, hash: String = "#!") : Navigo(root, useHash, hash) {

    companion object {
        /**
         * Init default routing.
         */
        fun start() {
            routing = Routing()
        }

        /**
         * Shutdown default routing.
         */
        fun shutdown() {
            routing.destroy()
        }
    }
}

/**
 * Default JavaScript router.
 */
var routing = Routing().also { it.resolve() }
