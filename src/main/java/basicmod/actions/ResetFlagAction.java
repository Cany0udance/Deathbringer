package basicmod.actions;

import basicmod.cards.skills.Arsenal;
import com.megacrit.cardcrawl.actions.AbstractGameAction;

public class ResetFlagAction extends AbstractGameAction {
    private Arsenal cardInstance;

    public ResetFlagAction(Arsenal cardInstance) {
        this.cardInstance = cardInstance;
    }

    @Override
    public void update() {
        cardInstance.setTriggeredDraw(false);
        cardInstance.triggerArsenalDiscard();
        this.isDone = true;
    }
}
