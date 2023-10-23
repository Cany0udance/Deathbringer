package basicmod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;

import static basicmod.Deathbringer.makeID;

public class ThresholdPower extends BasePower {
    public static final String POWER_ID = makeID("ThresholdPower");

    public ThresholdPower(final AbstractCreature owner, final int amount) {
        super(POWER_ID, PowerType.BUFF, false, owner, amount);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }
}
