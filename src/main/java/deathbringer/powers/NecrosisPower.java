package deathbringer.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.PoisonPower;

import static deathbringer.Deathbringer.makeID;

public class NecrosisPower extends BasePower {
    public static final String POWER_ID = makeID("NecrosisPower");

    public NecrosisPower(final AbstractCreature owner, final int amount) {
        super(POWER_ID, PowerType.DEBUFF, true, owner, amount);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }
}
