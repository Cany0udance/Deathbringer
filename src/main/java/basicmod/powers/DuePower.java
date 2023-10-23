package basicmod.powers;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static basicmod.Deathbringer.makeID;

public class DuePower extends BasePower {
    public static final String POWER_ID = makeID("Due");

    public DuePower(AbstractCreature owner, int amount) {
        super(POWER_ID, PowerType.DEBUFF, true, owner, amount);
        updateDescription();
    }

    @Override
    public void atEndOfRound() {
        if (this.amount == 0) {
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, "Deathbringer:Due"));
        } else {
            this.addToBot(new ReducePowerAction(this.owner, this.owner, "Deathbringer:Due", 1));
            updateDescription();
        }
    }

    @Override
    public void updateDescription() {
        this.description = "When this enemy dies, gain #b" + this.amount + " #yStrength and #yDexterity, then transfer remaining Due on to another enemy.";
    }
}