package deathbringer.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;

import static deathbringer.Deathbringer.makeID;

public class TradePower extends BasePower {
    public static final String POWER_ID = makeID("TradePower");

    public TradePower(final AbstractCreature owner, final int amount) {
        super(POWER_ID, PowerType.BUFF, false, owner, amount);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

}
