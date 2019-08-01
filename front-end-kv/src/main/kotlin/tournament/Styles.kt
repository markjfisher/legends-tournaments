package tournament

import pl.treksoft.kvision.core.*
import pl.treksoft.kvision.utils.em
import pl.treksoft.kvision.utils.pc
import pl.treksoft.kvision.utils.px
import tournament.html.MyStyle.Companion.mystyle

object Styles {
    val sidePanelStyle = Style {
        width = 220.px
        height = 100.pc
        position = Position.FIXED
        zIndex = 1
        top = 0.px
        left = 0.px
        overflow = Overflow.HIDDEN
        paddingTop = 8.px
        paddingBottom = 8.px
        paddingLeft = 0.px
        paddingRight = 0.px
        background = Background(color = 0x1c1e23)
        mystyle("a") {
            paddingTop = 6.px
            paddingBottom = 8.px
            paddingLeft = 6.px
            paddingRight = 16.px
            textDecoration = TextDecoration(TextDecorationLine.NONE)
            fontSize = 22.px
            color = Color(0x7e838c)
            display = Display.BLOCK
        }

        mystyle("a:hover") {
            color = Color(0x064579)
        }
    }

    val mainStyle = Style {
        marginLeft = 220.px
        paddingTop = 0.px
        paddingBottom = 0.px
        paddingLeft = 0.px
        paddingRight = 0.px

    }

    internal var cardStyle = CardStyle()
    internal class CardStyle {
        val link = Style {
            textDecoration =  TextDecoration(line = TextDecorationLine.NONE)
        }

        val main = Style {
            width = 335.px
            minHeight = 350.px
            border = Border(width = 1.px, style = BorderStyle.SOLID, color = 0xcfcfcf)
        }

        val panel = Style {
            val paddingCommon = 5.px
            paddingTop = paddingCommon
            paddingBottom = paddingCommon
            paddingLeft = paddingCommon
            paddingRight = paddingCommon
        }
        val headerImageContainer = Style {
            height = 200.px
            overflow = Overflow.HIDDEN
        }

        internal var info = CardInfoStyle()
        internal class CardInfoStyle {
            val block = Style {
            }

            val gameName = Style {
                lineHeight = 2.em
            }

            val hr = Style {
                // borderBottom = Border(width = 1.px, style = BorderStyle.SOLID, color = 0x28303f)
                height = 1.px
                margin = 0.px
            }

            val title = Style {
                fontSize = 22.px
                paddingBottom = 10.px
                paddingTop = 2.px
            }

            val details = Style {
                // paddingRight = 100.px
            }

            val table = Style {
            }

            val row = Style {
                padding = 1.px
                fontSize = 9.px
            }
            val cell = Style {
                padding = 1.px
            }
        }
    }

    internal var editFormStyle = EditFormStyle()
    internal class EditFormStyle {
        val container = Style {
            // border = Border(width = 1.px, style = BorderStyle.SOLID, color = 0xffffff)
//            padding = 2.px
//            margin = 2.px
        }

        val inputPanel = Style {
            // border = Border(width = 1.px, style = BorderStyle.DASHED, color = 0x3c3c3c)
             padding = 2.px
             margin = 2.px
        }

        val infoPanel = Style {
            // border = Border(width = 1.px, style = BorderStyle.DASHED, color = 0x3c3c3c)
            // padding = 5.px
            // margin = 5.px
        }

        val tournamentForm = Style {
            // border = Border(width = 1.px, style = BorderStyle.DOTTED, color = 0x888888)
            // padding = 5.px
            // margin = 5.px
        }
    }
}