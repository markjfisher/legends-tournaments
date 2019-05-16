package tournament.api.view

import com.narbase.kunafa.core.components.*
import com.narbase.kunafa.core.css.*
import com.narbase.kunafa.core.dimensions.dependent.matchParent
import com.narbase.kunafa.core.dimensions.dependent.weightOf
import com.narbase.kunafa.core.dimensions.pc
import com.narbase.kunafa.core.dimensions.px
import com.narbase.kunafa.core.drawable.Color
import tournament.api.view.Home.Companion.HomeStyles.textStyle

class Home : Component() {
    // complementary from first 3 colours, based on samples from mech-dragon pic
    // site: https://www.sessions.edu/color-calculator/
    // grey: 767884, cream: e8cd9c, dark: 4d6175
    // greeny: 847d76, blue: b19ce8, reddy: 755c4d

    // From Legends site, + tetradic
    // b7a574,ffffff
    // b7b7b7,74b7b7

    override fun View?.getView() = horizontalLayout {
        style {

        }

        val menu = horizontalLayout {
            style {
                overflow = "hidden"
                backgroundColor = Color("333333")
                position = "fixed"
                top = 0.px
                width = matchParent
            }

            a {
                href = "#home"
                text = "Home"
                addRuleSet(HomeStyles.navbarA)
            }
            a {
                href = "#about"
                text = "About"
                addRuleSet(HomeStyles.navbarA)
            }
        }

        val tournamentTitle = verticalLayout {

            textView {
                text = "Legends Tournaments"
                addRuleSet(textStyle)

                style {
                    fontSize = 64.px
                    position = "fixed"
                    bottom = 10.pc
                    right = 10.pc
                }
            }
        }
    }

    companion object {
        object HomeStyles {
            val textStyle = classRuleSet {
                fontFamily = homeFontFamily
                color = Color("b7a574")
            }

            val navbarA = classRuleSet {
                fontFamily = homeFontFamily
                float = "left"
                display = "block"
                color = Color("f2f2f2")
                textAlign = TextAlign.Center
                padding = "14px 16px"
                textDecoration = "none"
                fontSize = 17.px
                hover {
                    background = "dddddd"
                    color = Color.black
                }
            }
        }

        private const val homeFontFamily = "'EB Garamond', 'Open Sans', sans-serif"
    }
}