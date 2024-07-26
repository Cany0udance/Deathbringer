package deathbringer.patches;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import deathbringer.powers.VirulencePower;
import deathbringer.relics.Deadsprout;
import javassist.CtBehavior;

import java.util.ArrayList;

@SpirePatch(
        clz = ApplyPowerAction.class,
        method = SpirePatch.CONSTRUCTOR,
        paramtypez = {AbstractCreature.class, AbstractCreature.class, AbstractPower.class, int.class, boolean.class, AbstractGameAction.AttackEffect.class}
)
public class DeadsproutPatch {
    public static void Postfix(ApplyPowerAction __instance, AbstractCreature target, AbstractCreature source, AbstractPower power, int stackAmount, boolean isFast, AbstractGameAction.AttackEffect effect) {
        // Check if the power being applied is Virulence, the source is the player, and the player has the Deadsprout relic.
        if (power.ID.equals(VirulencePower.POWER_ID) && source instanceof AbstractPlayer) {
            AbstractPlayer player = (AbstractPlayer) source;
            if (player.hasRelic(Deadsprout.ID)) {
                Deadsprout relic = (Deadsprout) player.getRelic(Deadsprout.ID);
                // Queue an action to grant Strength after the current action completes
                AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                    @Override
                    public void update() {
                        AbstractPlayer p = AbstractDungeon.player;
                        relic.flash();
                  //      this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, relic));
                        this.addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, 1), 1));
                        this.isDone = true; // Mark this action as complete
                    }
                });
            }
        }
    }
}
