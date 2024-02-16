package basicmod.patches;

import basicmod.powers.VirulencePower;
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
    public static void Postfix(ApplyPowerAction __instance, AbstractCreature target, AbstractCreature source, AbstractPower powerToApply, int stackAmount, boolean isFast, AbstractGameAction.AttackEffect effect) {
        // Check if the power being applied is Poison, the source is the player, and the target is not the player
        if (powerToApply.ID.equals(PoisonPower.POWER_ID) && source != null && source.isPlayer && target != source) {
            // Check if the player has the Virulence power
            AbstractPower virulence = source.getPower(VirulencePower.POWER_ID);
            if (virulence != null) {
                // Add additional Poison based on the amount of Virulence stacks the player has
                int additionalPoison = virulence.amount;
                __instance.amount += additionalPoison; // Increase the amount of Poison being applied
                powerToApply.amount += additionalPoison; // Update the powerToApply object as well

                // Optional: Flash Virulence power to indicate it's being used
                virulence.flash();
            }
        }
    }
}
