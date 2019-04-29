package tournament.api

import io.elderscrollslegends.Card
import io.elderscrollslegends.Deck
import org.assertj.core.api.Assertions.assertThat
import org.jeasy.rules.api.Facts
import org.jeasy.rules.api.Rules
import org.jeasy.rules.core.DefaultRulesEngine
import org.jeasy.rules.mvel.MVELRule
import org.junit.jupiter.api.Test

class RulesTesting {

    @Test
    fun `given a deck with an epic it should not be allowed`() {
        val card = Card(name = "test card", id = "1", rarity = "Epic")
        val deck = Deck(listOf(card))

        val rule = MVELRule()
            .name("no epics rule")
            .description("No epics allowed in deck")
            .`when`("epicCount > 0")
            .then("valid.setValid(false);")

        val epicCount = deck.cards.count { it.rarity == "Epic" }

        val valid = Valid()
        val facts = Facts()
        facts.put("epicCount", epicCount)
        facts.put("valid", valid)

        val rules = Rules(rule)
        val rulesEngine = DefaultRulesEngine()
        rulesEngine.fire(rules, facts)

        assertThat(valid.valid).isFalse()

    }

    data class Valid(
        var valid: Boolean = true
    )
}

