package basicmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import static basemod.BaseMod.logger;

import static basicmod.Deathbringer.makeID;

public class StickyBombPower extends BasePower implements NonStackablePower {
    public static final String POWER_ID = makeID("StickyBomb");
    private static final AbstractPower.PowerType TYPE = AbstractPower.PowerType.DEBUFF;
    private static final boolean TURN_BASED = true;


    private final int damage;

    public StickyBombPower(AbstractCreature owner, int turns, int damage) {
        super(POWER_ID, TYPE, TURN_BASED, owner, turns);
        this.name = "Sticky Bomb"; // Hard-code the name
        this.damage = damage;
        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        addToBot(new ReducePowerAction(this.owner, this.owner, this, 1));
        if (this.amount == 1) {
            addToBot(new DamageAction(owner, new DamageInfo(owner, damage, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE));
            addToBot(new DrawCardAction(2));
        }
    }

    private static final String[] DESCRIPTIONS = {
            "In #b%d turns, #b%d damage will be dealt to this enemy.",
            "In #b1 turn, #b%d damage will be dealt to this enemy."
    };

    @Override
    public void updateDescription() {
        if (this.amount == 1) {
            description = "In #b1 turn, #b" + this.damage + " damage will be dealt to this enemy.";
        } else {
            description = "In #b" + this.amount + " turns, #b" + this.damage + " damage will be dealt to this enemy.";
        }
    }
}