package deathbringer.patches;

import deathbringer.powers.VirulencePower;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;

@SpirePatch(
        clz = ApplyPowerAction.class,
        method = SpirePatch.CONSTRUCTOR,
        paramtypez = {AbstractCreature.class, AbstractCreature.class, AbstractPower.class, int.class, boolean.class, AbstractGameAction.AttackEffect.class}
)
public class VirulencePatch {
    public static void Postfix(ApplyPowerAction instance, AbstractCreature target, AbstractCreature source, AbstractPower powerToApply, int stackAmount, boolean isFast, AbstractGameAction.AttackEffect effect) {
        if (powerToApply.ID.equals(PoisonPower.POWER_ID) && source != null && source.isPlayer && target != source) {
            AbstractPower virulence = source.getPower(VirulencePower.POWER_ID);
            if (virulence != null) {
                int additionalPoison = virulence.amount;
                instance.amount += additionalPoison; // Increase the amount of Poison being applied
                powerToApply.amount += additionalPoison; // Update the powerToApply object as well
                virulence.flash();
            }
        }
    }
}