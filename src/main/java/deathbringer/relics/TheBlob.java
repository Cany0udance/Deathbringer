package deathbringer.relics;

import deathbringer.Deathbringer;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class TheBlob extends BaseRelic {
    private static final String NAME = "TheBlob";
    public static final String ID = Deathbringer.makeID(NAME);
    private static final RelicTier RARITY = RelicTier.SHOP; // Feel free to adjust rarity
    private static final LandingSound SOUND = LandingSound.MAGICAL;

    public TheBlob() {
        super(ID, NAME, deathbringer.character.Deathbringer.Enums.CARD_COLOR, RARITY, SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0]; // Ensure you have a description in your localization files
    }

    @Override
    public void onPlayerEndTurn() {
        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            if (AbstractDungeon.player.hasPower(PoisonPower.POWER_ID)) {
                flash(); // Visual effect to indicate relic activation
                addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                    if (!m.isDead && !m.isDying && m.hasPower(PoisonPower.POWER_ID)) {
                        int poisonAmount = m.getPower(PoisonPower.POWER_ID).amount;
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, AbstractDungeon.player, new PoisonPower(m, AbstractDungeon.player, poisonAmount), poisonAmount));
                    }
                }
                if (AbstractDungeon.player.hasPower(PoisonPower.POWER_ID)) {
                    int playerPoisonAmount = AbstractDungeon.player.getPower(PoisonPower.POWER_ID).amount;
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new PoisonPower(AbstractDungeon.player, AbstractDungeon.player, playerPoisonAmount), playerPoisonAmount));
                }
            }
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new TheBlob();
    }
}
