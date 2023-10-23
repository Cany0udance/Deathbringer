package basicmod.actions;

import basicmod.cards.skills.Shroud;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class ShroudAction extends AbstractGameAction {
    private final int increaseAmount;
    private final AbstractCard card;

    public ShroudAction(AbstractCard card, int increaseAmount) {
        this.card = card;
        this.increaseAmount = increaseAmount;
    }

    @Override
    public void update() {
        card.baseBlock += increaseAmount;
        card.applyPowers();

        for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
            if (c instanceof Shroud) {
                c.baseBlock += increaseAmount;
                c.applyPowers();
            }
        }

        for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
            if (c instanceof Shroud) {
                c.baseBlock += increaseAmount;
                c.applyPowers();
            }
        }

        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c instanceof Shroud) {
                c.baseBlock += increaseAmount;
                c.applyPowers();
            }
        }

        this.isDone = true;
    }
}