package deathbringer.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;

import static deathbringer.Deathbringer.makeID;

public class VirulencePower extends BasePower {
    public static final String POWER_ID = makeID("VirulencePower");
    public VirulencePower(AbstractCreature owner, int amount) {
        super(POWER_ID, PowerType.BUFF, false, owner, amount);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = String.format(DESCRIPTIONS[0], amount);
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        updateDescription();
    }
}