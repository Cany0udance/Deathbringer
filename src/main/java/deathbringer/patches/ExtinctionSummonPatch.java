package deathbringer.patches;

import basemod.ReflectionHacks;
import deathbringer.character.Deathbringer;
import deathbringer.powers.ExtinctionMarkPower;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.unique.SummonGremlinAction;
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
