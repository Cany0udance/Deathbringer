package basicmod.relics;

import basicmod.Deathbringer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class RepairedCompass extends BaseRelic {
    private static final String NAME = "RepairedCompass";
    public static final String ID = Deathbringer.makeID(NAME);
    private static final RelicTier RARITY = RelicTier.BOSS;
    private static final LandingSound SOUND = LandingSound.FLAT;

    public RepairedCompass() {
        super(ID, NAME, basicmod.character.Deathbringer.Enums.CARD_COLOR, RARITY, SOUND);
    }

    public boolean canSpawn() {
        return AbstractDungeon.player.hasRelic("Deathbringer:ShatteredCompass");
    }

    @Override
    public void obtain() {
        if (AbstractDungeon.player.hasRelic(ShatteredCompass.ID)) {
            for (int i = 0; i < AbstractDungeon.player.relics.size(); ++i) {
                if (AbstractDungeon.player.relics.get(i).relicId.equals(ShatteredCompass.ID)) {
                    instantObtain(AbstractDungeon.player, i, true);
                    break;
                }
            }
        } else {
            super.obtain();
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new RepairedCompass();
    }
}