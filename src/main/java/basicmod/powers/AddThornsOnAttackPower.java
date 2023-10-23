package basicmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.evacipated.cardcrawl.mod.stslib.patches.NeutralPowertypePatch;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ThornsPower;

public class AddThornsOnAttackPower extends BasePower implements InvisiblePower, CloneablePowerInterface {
    public static final String POWER_ID = "Deathbringer:AddThornsOnAttackPower";

    public AddThornsOnAttackPower(final AbstractCreature owner) {
        super(POWER_ID, NeutralPowertypePatch.NEUTRAL, false, owner, 1);
        this.ID = POWER_ID;
        this.name = "Add Thorns on Attack"; // Won't be visible due to InvisiblePower
        this.owner = owner;
        this.type = NeutralPowertypePatch.NEUTRAL;
        this.isTurnBased = false;
        this.amount = 1;  // The amount of Thorns to add
    }
    private int attacksThisTurn = 0;  // Counter to keep track of attacks each turn
    public static int architectsNailHitCounter = 0;

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.type != DamageInfo.DamageType.THORNS && info.type != DamageInfo.DamageType.HP_LOSS && info.owner != null && info.owner != this.owner) {
            this.attacksThisTurn += 1;  // Increment the counter
        }
        architectsNailHitCounter++;
        return damageAmount;
    }

    @Override
    public void atEndOfRound() {
        if (this.attacksThisTurn > 0) {
            this.flash();
            // Add the total Thorns to the player at the end of the round
            this.addToBot(new ApplyPowerAction(this.owner, this.owner, new ThornsPower(this.owner, this.attacksThisTurn), this.attacksThisTurn));
        }
        // Reset the counter
        this.attacksThisTurn = 0;
    }

    @Override
    public AbstractPower makeCopy() {
        return new AddThornsOnAttackPower(this.owner);
    }
}
