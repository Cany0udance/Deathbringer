package basicmod.patches;

import basemod.ReflectionHacks;
import basicmod.character.Deathbringer;
import basicmod.powers.ExtinctionMarkPower;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import static basemod.BaseMod.logger;

@SpirePatch(
        clz = SpawnMonsterAction.class,
        method = "update"
)
public class ExtinctionSummonSlimePatch {
    public static void Postfix(SpawnMonsterAction __instance) {
        // Extract the private "m" field using ReflectionHacks
        AbstractMonster m = (AbstractMonster) ReflectionHacks.getPrivate(__instance, SpawnMonsterAction.class, "m");

        if (m != null && Deathbringer.enemiesMarkedForExtinction.containsKey(m.id) &&
                Deathbringer.enemiesMarkedForExtinction.get(m.id)) {
            ExtinctionMarkPower newPower = new ExtinctionMarkPower(m);
            m.addPower(newPower);
        }
    }
}
