package tournament.api.kunafa.view

import com.narbase.kunafa.core.components.*
import com.narbase.kunafa.core.components.layout.LinearLayout
import com.narbase.kunafa.core.css.*
import com.narbase.kunafa.core.dimensions.dependent.matchParent
import com.narbase.kunafa.core.dimensions.dependent.weightOf
import com.narbase.kunafa.core.dimensions.dependent.wrapContent
import com.narbase.kunafa.core.dimensions.px
import com.narbase.kunafa.core.drawable.Color
import com.narbase.kunafa.core.lifecycle.LifecycleOwner
import tournament.api.kunafa.model.TournamentModel
import tournament.api.kunafa.viewmodel.TournamentViewModel

class TournamentComponent(private val viewModel: TournamentViewModel) : Component() {
    private var listLayout: LinearLayout? = null
    private var tournamentTextInput: TextInput? = null
    private val tournamentViews = mutableMapOf<Int, TournamentItem>()

    override fun onViewMounted(lifecycleOwner: LifecycleOwner) {
        console.log("View mounted, setting up observers")
        viewModel.onItemAdded.observe(::addItem)
        viewModel.onItemDeleted.observe(::deleteItem)
        viewModel.onItemUpdated.observe(::updateItem)
    }

    private fun onButtonClicked() {
        viewModel.addNewTournament(tournamentTextInput?.text)
        tournamentTextInput?.text = ""
    }

    private fun addItem(pm: TournamentModel?) {
        pm ?: return
        val component = TournamentItem(pm, viewModel::deleteItem, viewModel::toggleItemStatus)
        listLayout?.mount(component)
        tournamentViews[pm.id] = component
    }

    private fun deleteItem(pm: TournamentModel?) {
        pm ?: return
        val component = tournamentViews[pm.id] ?: return
        listLayout?.unMount(component)
        tournamentViews.remove(pm.id)
    }

    private fun updateItem(pm: TournamentModel?) {
        pm ?: return
        val component = tournamentViews[pm.id] ?: return
        if (pm.isDone) {
            component.markDone()
        } else {
            component.markUndone()
        }
    }

    override fun View?.getView() = horizontalLayout {
        style {
            width = matchParent
            height = matchParent
        }

        verticalLayout {
            style {
                width = weightOf(1)
                minWidth = 200.px
                height = matchParent
                backgroundColor = Color.white
                padding = 32.px.toString()
                alignItems = Alignment.Center
            }

            textView {
                text = "Legends Tournaments"
                style {
                    fontSize = 32.px
                    color = Color(100, 240, 100)
                }
            }

            tournamentTextInput = textInput {
                style {
                    width = matchParent
                    backgroundColor = Color("#fafafa")
                    border = "1px solid #efefef"
                    padding = 8.px.toString()
                    borderRadius = 4.px.toString()
                    marginTop = 16.px

                }
                placeholder = "Enter a tournament name"
            }

            button {
                id = "myButton"
                text = "Add to tournament"
                style {
                    marginTop = 16.px
                }
                onClick = {
                    onButtonClicked()
                }
            }
        }

        verticalScrollLayout {
            style {
                width = weightOf(2)
                height = matchParent
                backgroundColor = Color("#ededed")
            }

            listLayout = verticalLayout {
                style {
                    width = matchParent
                    height = wrapContent
                    padding = 8.px.toString()
                }
            }
        }
    }

    override fun onViewRemoved(lifecycleOwner: LifecycleOwner) {
        console.log("View removed")
    }

}