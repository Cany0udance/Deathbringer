package basicmod.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

import static basicmod.Deathbringer.makeID;

public class OpeningPower extends BasePower {
    public static final String POWER_ID = makeID("OpeningPower");

    public OpeningPower(AbstractCreature owner, int vigorAmount) {
        super(POWER_ID, PowerType.BUFF, false, owner, vigorAmount);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], this.amount);
    }

    @Override
    public void atStartOfTurnPostDraw() {
        boolean enemyAttacking = false;

        for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (m.getIntentBaseDmg() >= 0) {
                enemyAttacking = true;
                break;
            }
        }

        if (!enemyAttacking) {
            addToBot(new ApplyPowerAction(owner, owner, new VigorPower(owner, amount), amount));
        }
    }
}
