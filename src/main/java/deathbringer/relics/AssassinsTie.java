package deathbringer.relics;

import deathbringer.Deathbringer;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.Iterator;

public class AssassinsTie extends BaseRelic {
    private static final String NAME = "AssassinsTie";
    public static final String ID = Deathbringer.makeID(NAME);
    private static final RelicTier RARITY = RelicTier.RARE; // Adjust as needed
    private static final LandingSound SOUND = LandingSound.FLAT;

    public AssassinsTie() {
        super(ID, NAME, deathbringer.character.Deathbringer.Enums.CARD_COLOR, RARITY, SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0]; // Make sure you have the description ready in your localization files
    }

    @Override
    public void atPreBattle() {
        boolean isEliteOrBoss = AbstractDungeon.getCurrRoom().eliteTrigger;
        Iterator var2 = AbstractDungeon.getMonsters().monsters.iterator();

        while(var2.hasNext()) {
            AbstractMonster m = (AbstractMonster)var2.next();
            if (m.type == AbstractMonster.EnemyType.BOSS) {
                isEliteOrBoss = true;
            }
        }

        if (isEliteOrBoss) {
            this.beginLongPulse();
            this.flash();
        }
    }

    @Override
    public void atTurnStartPostDraw() {
        boolean isEliteOrBoss = AbstractDungeon.getCurrRoom().eliteTrigger;
        Iterator var2 = AbstractDungeon.getMonsters().monsters.iterator();

        while(var2.hasNext()) {
            AbstractMonster m = (AbstractMonster)var2.next();
            if (m.type == AbstractMonster.EnemyType.BOSS) {
                isEliteOrBoss = true;
            }
        }

        if (isEliteOrBoss) {
            AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player, 1));
        }
    }

    @Override
    public void onVictory() {
        if (this.pulse) {
            this.stopPulse();
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new AssassinsTie();
    }
}

