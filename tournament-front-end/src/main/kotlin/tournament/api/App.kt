package tournament.api

import com.narbase.kunafa.core.components.*
import com.narbase.kunafa.core.css.height
import com.narbase.kunafa.core.css.width
import com.narbase.kunafa.core.dimensions.px
import com.narbase.kunafa.core.lifecycle.LifecycleOwner
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.UnstableDefault

@UnstableDefault
fun main() {
    GlobalScope.launch {
        Stuff().getThing()
        val postThing = Stuff().postThing(Thing(name = "post thing from web"))
        console.log("post: got $postThing")
        val putThing = Stuff().putThing(Thing(name = "put thing from web"))
        console.log("post: got $putThing")
    }

    page {
        mount(AppView())
        verticalLayout {
            userDetailsView("https://cdn-images-1.medium.com/fit/c/60/60/1*D2ppCycgCvm90WV3CNIM-A.png", "Mark Fisher 1")
            userDetailsView("https://cdn-images-1.medium.com/fit/c/60/60/1*D2ppCycgCvm90WV3CNIM-A.png", "Mark Fisher 2")
            userDetailsView("https://cdn-images-1.medium.com/fit/c/60/60/1*D2ppCycgCvm90WV3CNIM-A.png", "Mark Fisher 3")
        }
    }
}


fun View.userDetailsView(imageUrl: String, userFullName: String) {
    horizontalLayout {
        imageView {
            element.src = imageUrl
            style {
                width = 50.px
                height = 50.px
            }
        }
        textView {
            text = userFullName
        }
    }
}

class AppView: Component() {
    override fun View?.getView(): View {
        return horizontalLayout {
            textView {
                text = "Some text"
            }
        }
    }

    override fun onViewMounted(lifecycleOwner: LifecycleOwner) {
        console.log("View mounted")
    }

    override fun onViewRemoved(lifecycleOwner: LifecycleOwner) {
        console.log("View removed")
    }
}
