import pl.treksoft.jquery.jQuery
import kotlin.browser.document

interface TestRunner {
    fun beforeTest()

    fun afterTest()

    fun run(code: () -> Unit) {
        beforeTest()
        code()
        afterTest()
    }
}

interface DomTest : TestRunner {

    override fun beforeTest() {
        val fixture = "<div style=\"display: none\" id=\"pretest\">" +
                "<div id=\"tournament\"></div></div>"
        document.body?.insertAdjacentHTML("afterbegin", fixture)
    }

    override fun afterTest() {
        val div = document.getElementById("pretest")
        div?.remove()
        jQuery(`object` = ".modal-backdrop").remove()
    }

}
