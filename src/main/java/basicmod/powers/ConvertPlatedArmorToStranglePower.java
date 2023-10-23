package basicmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnMyBlockBrokenPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;

import static basicmod.Deathbringer.makeID;

public class ConvertPlatedArmorToStranglePower extends BasePower implements OnMyBlockBrokenPower, CloneablePowerInterface {
    public static final String POWER_ID = makeID("ConvertPlatedArmorToStrangle");
    private static final AbstractPower.PowerType TYPE = AbstractPower.PowerType.DEBUFF;
    private static final boolean TURN_BASED = false;

    public ConvertPlatedArmorToStranglePower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
    }

    @Override
    public void onMyBlockBroken() {
        int strangleAmount = 0;
        if (this.owner.hasPower(PlatedArmorPower.POWER_ID)) {
            strangleAmount = this.owner.getPower(PlatedArmorPower.POWER_ID).amount;
        }
        if (strangleAmount > 0) {
            this.addToBot(new ApplyPowerAction(this.owner, this.owner, new StranglePower(this.owner, strangleAmount), strangleAmount));
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, PlatedArmorPower.POWER_ID));
        }
        this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    @Override
    public AbstractPower makeCopy() {
        return new ConvertPlatedArmorToStranglePower(this.owner, this.amount);
    }
}
