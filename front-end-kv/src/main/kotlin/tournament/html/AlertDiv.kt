package tournament.html

import pl.treksoft.kvision.core.Container
import pl.treksoft.kvision.html.Button.Companion.button
import pl.treksoft.kvision.html.Div
import pl.treksoft.kvision.html.Div.Companion.div
import pl.treksoft.kvision.utils.rem

class AlertDiv {
    companion object {
        fun Container.createAlertDiv(
            buttonId: String? = null,
            type: AlertDivType = AlertDivType.WARNING,
            init: (Div.() -> Unit)? = null
        ): Div {
            return div(classes = setOf("alert", "alert-dismissible", type.className)) {
                maxWidth = 20.rem
                div { // div is currently required to not propogate the attribute up to the top level div - see https://github.com/rjaros/kvision/issues/50
                    button(text = "×", classes = setOf("close")) {
                        setAttribute("data-dismiss", "alert")
                        if (buttonId != null) setAttribute("id", buttonId)
                    }
                }
            }.apply { init?.invoke(this) }
        }
    }
}

enum class AlertDivType(val className: String) {
    WARNING("alert-warning"),
    DANGER("alert-danger"),
    SUCCESS("alert-success"),
    INFO("alert-info")
}

/*
<div class="alert alert-dismissible alert-warning">
  <button type="button" class="close" data-dismiss="alert">&times;</button>
  <h4>Warning!</h4>
  <p>Best check yo self, you're not looking too good. Nulla vitae elit libero, a pharetra augue. Praesent commodo cursus magna, <a href="#" class="alert-link">vel scelerisque nisl consectetur et</a>.</p>
</div>

<div class="alert alert-dismissible alert-danger">
  <button type="button" class="close" data-dismiss="alert">&times;</button>
  <strong>Oh snap!</strong> <a href="#" class="alert-link">Change a few things up</a> and try submitting again.
</div>

<div class="alert alert-dismissible alert-success">
  <button type="button" class="close" data-dismiss="alert">&times;</button>
  <strong>Well done!</strong> You successfully read <a href="#" class="alert-link">this important alert message</a>.
</div>

<div class="alert alert-dismissible alert-info">
  <button type="button" class="close" data-dismiss="alert">&times;</button>
  <strong>Heads up!</strong> This <a href="#" class="alert-link">alert needs your attention</a>, but it's not super important.
</div>
 */