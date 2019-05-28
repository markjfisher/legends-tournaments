package tournament

import pl.treksoft.kvision.hmr.ApplicationBase
import pl.treksoft.kvision.html.H4
import pl.treksoft.kvision.html.H4.Companion.h4
import pl.treksoft.kvision.i18n.DefaultI18nManager
import pl.treksoft.kvision.i18n.I18n
import pl.treksoft.kvision.panel.FlexDir
import pl.treksoft.kvision.panel.FlexJustify
import pl.treksoft.kvision.panel.FlexPanel.Companion.flexPanel
import pl.treksoft.kvision.panel.Root
import tournament.html.Card
import tournament.html.Card.Companion.createCard

object Tournament : ApplicationBase {
    private lateinit var root: Root

    override fun start(state: Map<String, Any>) {

        I18n.manager =
            DefaultI18nManager(
                mapOf(
                    "en" to pl.treksoft.kvision.require("./messages-en.json")
                )
            )

        root = Root("tournament") {
            flexPanel(FlexDir.ROW, justify = FlexJustify.CENTER) {
                createCard(
                    Card(
                        cardHeader = "First Header",
                        cardTitle = "First Title",
                        cardText = "This is the main text"
                    )
                )
                h4(content = "a h4")
            }
        }
    }

    override fun dispose(): Map<String, Any> {
        root.dispose()
        return mapOf()
    }

    val css = pl.treksoft.kvision.require("./css/tournament.css")
}

/*
<div class="card text-white bg-primary mb-3" style="max-width: 20rem;">
<div class="card-header">Header</div>
<div class="card-body">
<h4 class="card-title">Primary card title</h4>
<p class="card-text">Some quick example text to build on the card title and make up the bulk of the card's content.</p>
</div>
</div>
<div class="card text-white bg-secondary mb-3" style="max-width: 20rem;">
<div class="card-header">Header</div>
<div class="card-body">
<h4 class="card-title">Secondary card title</h4>
<p class="card-text">Some quick example text to build on the card title and make up the bulk of the card's content.</p>
</div>
</div>
<div class="card text-white bg-success mb-3" style="max-width: 20rem;">
<div class="card-header">Header</div>
<div class="card-body">
<h4 class="card-title">Success card title</h4>
<p class="card-text">Some quick example text to build on the card title and make up the bulk of the card's content.</p>
</div>
</div>
<div class="card text-white bg-danger mb-3" style="max-width: 20rem;">
<div class="card-header">Header</div>
<div class="card-body">
<h4 class="card-title">Danger card title</h4>
<p class="card-text">Some quick example text to build on the card title and make up the bulk of the card's content.</p>
</div>
</div>
<div class="card text-white bg-warning mb-3" style="max-width: 20rem;">
<div class="card-header">Header</div>
<div class="card-body">
<h4 class="card-title">Warning card title</h4>
<p class="card-text">Some quick example text to build on the card title and make up the bulk of the card's content.</p>
</div>
</div>
<div class="card text-white bg-info mb-3" style="max-width: 20rem;">
<div class="card-header">Header</div>
<div class="card-body">
<h4 class="card-title">Info card title</h4>
<p class="card-text">Some quick example text to build on the card title and make up the bulk of the card's content.</p>
</div>
</div>
<div class="card bg-light mb-3" style="max-width: 20rem;">
<div class="card-header">Header</div>
<div class="card-body">
<h4 class="card-title">Light card title</h4>
<p class="card-text">Some quick example text to build on the card title and make up the bulk of the card's content.</p>
</div>
</div>
<div class="card text-white bg-dark mb-3" style="max-width: 20rem;">
<div class="card-header">Header</div>
<div class="card-body">
<h4 class="card-title">Dark card title</h4>
<p class="card-text">Some quick example text to build on the card title and make up the bulk of the card's content.</p>
</div>
</div>
 */
