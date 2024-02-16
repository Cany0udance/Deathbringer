package basicmod.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.PoisonPower;

import static basicmod.Deathbringer.makeID;

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

    @Override
    public void atStartOfTurn() {
        // Apply Poison at the start of the player's turn
        if (!owner.isDeadOrEscaped()) {
            this.flash();
            addToBot(new ApplyPowerAction(owner, owner, new PoisonPower(owner, owner, this.amount), this.amount));
        }
    }
}
