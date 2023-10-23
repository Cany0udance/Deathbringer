package basicmod.patches;

import basemod.ReflectionHacks;
import basicmod.character.Deathbringer;
import basicmod.powers.ExtinctionMarkPower;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.unique.SummonGremlinAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

@SpirePatch(
        clz = SummonGremlinAction.class,
        method = "update"
)
public class ExtinctionSummonPatch {
    public static void Postfix(SummonGremlinAction __instance) {
        // Extract the private "m" field using ReflectionHacks
        AbstractMonster m = (AbstractMonster) ReflectionHacks.getPrivate(__instance, SummonGremlinAction.class, "m");

        if (m != null && Deathbringer.enemiesMarkedForExtinction.containsKey(m.id) &&
                Deathbringer.enemiesMarkedForExtinction.get(m.id)) {
            ExtinctionMarkPower newPower = new ExtinctionMarkPower(m);
            m.addPower(newPower);
        }
    }
}
