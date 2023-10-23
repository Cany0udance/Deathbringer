package basicmod.powers;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static basicmod.Deathbringer.makeID;

public class ExcelPower extends BasePower {
    public static final String POWER_ID = makeID("ExcelPower");

    private boolean hasLostHP;

    public ExcelPower(final AbstractCreature owner, final int amount) {
        super(POWER_ID, PowerType.BUFF, false, owner, amount);
        this.hasLostHP = false;
        updateDescription();
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (damageAmount > 0 && info.owner != null && info.owner != this.owner) {
            hasLostHP = true;
            addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
        return damageAmount;
    }

    @Override
    public void onVictory() {
        AbstractPlayer p = AbstractDungeon.player;
        if (p.currentHealth > 0 && !hasLostHP && this.amount > 0) {
            p.increaseMaxHp(this.amount, true);
        }
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], this.amount);
    }
}

