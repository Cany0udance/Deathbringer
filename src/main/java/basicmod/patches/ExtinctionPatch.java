package basicmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import basicmod.character.Deathbringer;
import basicmod.powers.ExtinctionMarkPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import static basemod.BaseMod.logger;

@SpirePatch(
        clz = MonsterGroup.class,
        method = "usePreBattleAction",
        paramtypez = {}
)
public class ExtinctionPatch {
    public static void Prefix(MonsterGroup __instance) {
        logger.info("Initializing MonsterGroup. Checking for enemies marked for extinction.");

        for (AbstractMonster m : __instance.monsters) {
            if (m != null) {
                if (Deathbringer.enemiesMarkedForExtinction.containsKey(m.id) &&
                        Deathbringer.enemiesMarkedForExtinction.get(m.id)) {
                    logger.info("Enemy with ID " + m.id + " is in the extinction list. Trying to apply ExtinctionMarkPower.");

                    ExtinctionMarkPower newPower = new ExtinctionMarkPower(m);
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, m, newPower, 1));

                    logger.info("Successfully applied power or so it claims.");

                    if (m.hasPower(ExtinctionMarkPower.POWER_ID)) {
                        logger.info("Verified: Power is actually on the monster.");
                    } else {
                        logger.info("Failed: Power is not on the monster.");
                    }
                } else {
                    logger.info("Enemy with ID " + m.id + " is not in the extinction list.");
                }
            } else {
                logger.info("AbstractMonster m is null");
            }
        }
    }
}
