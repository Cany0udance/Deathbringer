package deathbringer.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import deathbringer.powers.NecrosisPower;

import java.util.Iterator;

@SpirePatch(
        clz = ApplyPowerAction.class,
        method = SpirePatch.CONSTRUCTOR,
        paramtypez = {AbstractCreature.class, AbstractCreature.class, AbstractPower.class, int.class, boolean.class, AbstractGameAction.AttackEffect.class}
)
public class NecrosisPatch {
    public static void Postfix(ApplyPowerAction __instance, AbstractCreature target, AbstractCreature source, AbstractPower powerToApply, int stackAmount, boolean isFast, AbstractGameAction.AttackEffect effect) {
        // Check if the power being applied is Poison and the target is the player
        if (powerToApply.ID.equals(PoisonPower.POWER_ID) && target.isPlayer && !target.hasPower(ArtifactPower.POWER_ID)) {
            // Use an iterator to go through monsters from left to right
            Iterator<AbstractMonster> var1 = AbstractDungeon.getMonsters().monsters.iterator();
            while(var1.hasNext()) {
                AbstractMonster m = var1.next();
                if (!m.isDeadOrEscaped()) {
                    // Check if the monster has the Necrosis power
                    AbstractPower necrosis = m.getPower(NecrosisPower.POWER_ID);
                    if (necrosis != null) {
                        // Apply Poison based on the amount of Necrosis stacks
                        int poisonAmount = necrosis.amount;
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, AbstractDungeon.player, new PoisonPower(m, AbstractDungeon.player, poisonAmount), poisonAmount));
                        necrosis.flash();
                    }
                }
            }
        }
    }
}