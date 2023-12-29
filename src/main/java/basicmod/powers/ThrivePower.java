/*

package basicmod.powers;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static basicmod.Deathbringer.makeID;

public class ThrivePower extends BasePower {
    public static final String POWER_ID = makeID("ThrivePower");

    public ThrivePower(final AbstractCreature owner, final int amount) {
        super(POWER_ID, PowerType.BUFF, false, owner, amount);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], this.amount);
    }

    // This method would be called somewhere inside OutburstPower's stackPower method, when it "triggers".
    public void onOutburstTrigger() {
        this.flash();
        AbstractPlayer p = AbstractDungeon.player;
        addToBot(new HealAction(p, p, this.amount));
        addToBot(new RemoveSpecificPowerAction(p, p, this));
    }
}
*/