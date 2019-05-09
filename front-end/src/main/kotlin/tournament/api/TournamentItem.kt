package tournament.api

import com.narbase.kunafa.core.components.*
import com.narbase.kunafa.core.css.*
import com.narbase.kunafa.core.dimensions.dependent.matchParent
import com.narbase.kunafa.core.dimensions.dependent.weightOf
import com.narbase.kunafa.core.dimensions.px
import com.narbase.kunafa.core.drawable.Color

class TournamentItem(
    private val tournamentPM: TournamentPM,
    private val onDeleteClicked: (id: Int) -> Unit,
    private val onToggleState: (id: Int) -> Unit
) : Component() {
    private var checkboxView: View? = null
    private var todoTextView: TextView? = null

    override fun View?.getView() = horizontalLayout {
        addRuleSet(Style.rootLayout)
        onClick = { onToggleState(tournamentPM.id) }

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

    fun markDone() {
        checkboxView?.addRuleSet(Style.circleDone)
        todoTextView?.addRuleSet(Style.textDone)
    }

    fun markUndone() {
        checkboxView?.removeRuleSet(Style.circleDone)
        todoTextView?.removeRuleSet(Style.textDone)
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
            val circleDone = classRuleSet {
                backgroundColor = Color(100, 240, 100)
                border = "1px solid ${Color(100, 240, 100).toCss()}"
            }
            val textDone = classRuleSet {
                textDecoration = "line-through"
                color = Color("#ccc")
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