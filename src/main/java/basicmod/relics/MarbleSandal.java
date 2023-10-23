package basicmod.relics;

import basicmod.Deathbringer;
import basicmod.powers.DuePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerToRandomEnemyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class MarbleSandal extends BaseRelic {
    private static final String NAME = "MarbleSandal";
    public static final String ID = Deathbringer.makeID(NAME);
    private static final RelicTier RARITY = RelicTier.UNCOMMON;
    private static final LandingSound SOUND = LandingSound.CLINK;
    private boolean initialApplication = true;

    public MarbleSandal() {
        super(ID, NAME, basicmod.character.Deathbringer.Enums.CARD_COLOR, RARITY, SOUND);
    }

    @Override
    public void atBattleStart() {
        // Apply 3 Due to a random enemy if there's more than one enemy
        if (AbstractDungeon.getMonsters().monsters.size() > 1) {
            this.addToBot(new ApplyPowerToRandomEnemyAction(AbstractDungeon.player, new DuePower((AbstractCreature) null, 3), 3, false, AbstractGameAction.AttackEffect.NONE));
            if (initialApplication) {
                this.flash();
                this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                initialApplication = false; // set flag to false after initial application
            }
        }
    }

    @Override
    public void onMonsterDeath(AbstractMonster m) {
        if (m.hasPower(DuePower.POWER_ID)) {
            int amount = m.getPower(DuePower.POWER_ID).amount;
            // Grant Strength and Dexterity to player
            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, amount), amount));
            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, amount), amount));

            if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                // Apply Due to a random enemy
                this.addToBot(new ApplyPowerToRandomEnemyAction(AbstractDungeon.player, new DuePower((AbstractCreature) null, amount), amount, false, AbstractGameAction.AttackEffect.NONE));
            }
        }
    }

    @Override
    public void justEnteredRoom(AbstractRoom room) {
        initialApplication = true; // reset the flag upon entering a new room
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new MarbleSandal();
    }
}
