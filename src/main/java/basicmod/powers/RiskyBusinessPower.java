package basicmod.powers;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;

import static basicmod.Deathbringer.makeID;

public class RiskyBusinessPower extends BasePower {
    public static final String POWER_ID = makeID("RiskyBusinessPower");

    public RiskyBusinessPower(final AbstractCreature owner, final int amount) {
        super(POWER_ID, PowerType.BUFF, false, owner, amount);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        String cardWord = (this.amount == 1) ? "card" : "cards";
        this.description = String.format(DESCRIPTIONS[0], this.amount, cardWord);
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (power.ID.equals(PoisonPower.POWER_ID) && target.isPlayer) {
            addToBot(new DrawCardAction(this.amount));
        }
    }
}
