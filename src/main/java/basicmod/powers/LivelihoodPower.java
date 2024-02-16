package basicmod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;

import static basicmod.Deathbringer.makeID;

public class LivelihoodPower extends BasePower {
    public static final String POWER_ID = makeID("LivelihoodPower");

    public LivelihoodPower(final AbstractCreature owner, final int amount) {
        super(POWER_ID, PowerType.BUFF, false, owner, amount);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + this.amount + (this.amount == 1 ? DESCRIPTIONS[1] : DESCRIPTIONS[2]);
    }
}
