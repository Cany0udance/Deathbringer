package deathbringer.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

import static deathbringer.Deathbringer.makeID;

public class DominatePower extends BasePower {
    public static final String POWER_ID = makeID("DominatePower");

    public DominatePower(AbstractCreature owner) {
        super(POWER_ID, PowerType.BUFF, false, owner, 1);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = String.format(DESCRIPTIONS[0], amount, amount);
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        updateDescription();
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {

        if (power.ID.equals(PoisonPower.POWER_ID) && source == owner) {
            addToBot(new ApplyPowerAction(target, owner, new WeakPower(target, amount, false), amount));
            addToBot(new ApplyPowerAction(target, owner, new VulnerablePower(target, amount, false), amount));
        }
    }
}
