package deathbringer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class HarvestDrawAction extends AbstractGameAction {
    HarvestDrawAction(int reduction) {
        amount = reduction;
    }

    @Override
    public void update() {
        for (AbstractCard c : DrawCardAction.drawnCards) {
            if (c.cost >= 0) {
                c.setCostForTurn(amount);
                c.isCostModifiedForTurn = true;
                c.superFlash();
            }
        }
        isDone = true;
    }
}
