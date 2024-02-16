package basicmod.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

import static basicmod.Deathbringer.makeID;

public class RefinementPower extends BasePower {
    public static final String POWER_ID = makeID("RefinementPower");

    public RefinementPower(final AbstractCreature owner, final int amount) {
        super(POWER_ID, PowerType.BUFF, false, owner, amount);
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        flash(); // Visual effect to indicate the power activation
        addToBot(new ApplyPowerAction(owner, owner, new VirulencePower(owner, amount), amount));
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }
}
