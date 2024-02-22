package deathbringer.actions;

import deathbringer.Deathbringer;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class CleanKillAction extends AbstractGameAction {
    private DamageInfo info;

    public CleanKillAction(AbstractMonster target, DamageInfo info) {
        this.info = info;
        this.target = target;
        this.actionType = ActionType.DAMAGE;
    }

    public void update() {
        if (target.currentHealth == info.output && !target.isDying) {
            // Do stuff to grant permanent Strength here
            // Update your custom character-level Strength variable
            Deathbringer.addPermanentStrength(1);
        }
        addToTop(new DamageAction(target, info));
        isDone = true;
    }
}
