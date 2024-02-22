package deathbringer.powers;

import deathbringer.actions.AssassinFormAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

import static deathbringer.Deathbringer.makeID;

public class AssassinFormPower extends BasePower {
    public static final String POWER_ID = makeID("AssassinFormPower");
    private boolean flashPower;

    // A list containing the IDs of all cards with a triggerShadowplayEffect method

    public AssassinFormPower(AbstractCreature owner) {
        super(POWER_ID, PowerType.BUFF, false, owner, 1);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        if (amount == 1) {
            description = DESCRIPTIONS[0];  // "At the start of your turn, trigger each Shadowplay effect in your hand."
        } else {
            description = DESCRIPTIONS[0] + " #b" + amount + DESCRIPTIONS[1];  // "At the start of your turn, trigger each Shadowplay effect in your hand # times."
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        updateDescription();
    }

    @Override
    public void atStartOfTurnPostDraw() {
        flashPower = false; // Reset the flash indicator before the turn starts
        addToBot(new AssassinFormAction(this.amount, this)); // Pass `this` to be able to update `flashPower`
    }

    public void setFlash() {
        this.flashPower = true;
        this.flash(); // This method should make the power icon flash in the UI
    }
}
