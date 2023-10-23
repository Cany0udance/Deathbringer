package basicmod.powers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import basicmod.util.ShadowUtility;

import static basicmod.Deathbringer.makeID;

public class VeilPower extends BasePower {
    public static final String POWER_ID = makeID("VeilPower");

    public VeilPower(AbstractCreature owner) {
        super(POWER_ID, PowerType.BUFF, false, owner, 1);
    }

    @Override
    public void updateDescription() {
        int additionalTriggers = this.amount; // Assuming 'this.amount' contains the number of Veil powers stacked
        if (additionalTriggers == 1) {
            description = DESCRIPTIONS[0] + DESCRIPTIONS[1];
        } else {
            description = DESCRIPTIONS[0] + additionalTriggers + DESCRIPTIONS[2];
        }
    }

}
