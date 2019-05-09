package tournament.api

import com.narbase.kunafa.core.components.*
import com.narbase.kunafa.core.css.*
import com.narbase.kunafa.core.dimensions.dependent.matchParent
import com.narbase.kunafa.core.dimensions.dependent.weightOf
import com.narbase.kunafa.core.dimensions.px
import com.narbase.kunafa.core.drawable.Color

class TournamentItem(
    private val tournamentPM: TournamentPM,
    private val onDeleteClicked: (id: Int) -> Unit
) : Component() {
    private var checkboxView: View? = null
    private var todoTextView: TextView? = null

    override fun View?.getView() = horizontalLayout {
        addRuleSet(Style.rootLayout)

        checkboxView = view {
            addRuleSet(Style.circleBasic)
        }


        todoTextView = textView {
            style {
                width = weightOf(1)
                fontSize = 16.px
            }
            text = "${tournamentPM.text} (${tournamentPM.id})"
        }

        button {
            text = "Delete"
            onClick = { onDeleteClicked(tournamentPM.id) }
            addRuleSet(Style.deleteButtonStyle)
        }
    }

    companion object {
        object Style {
            val circleBasic = classRuleSet {
                width = 8.px
                height = 8.px
                borderRadius = 8.px.toString()
                border = "1px solid #888"
                marginRight = 8.px
            }
            val deleteButtonStyle = classRuleSet {
                borderRadius = 4.px.toString()
                backgroundColor = Color(230, 100, 100)
                color = Color.white
                padding = 4.px.toString()
                border = "none"
                cursor = "pointer"
                hover {
                    backgroundColor = Color(240, 40, 40)
                }
            }
            val rootLayout = classRuleSet {
                width = matchParent
                border = "1px solid #d4d4d4"
                marginTop = 8.px
                padding = 8.px.toString()
                alignItems = Alignment.Center
                cursor = "pointer"
                backgroundColor = Color.white
                hover {
                    boxShadow = "0px 4px 3px #bbb"
                }
            }
        }
    }
}