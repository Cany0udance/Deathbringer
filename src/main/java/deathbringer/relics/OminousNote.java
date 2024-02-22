package deathbringer.relics;

import deathbringer.Deathbringer;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class OminousNote extends BaseRelic {
    private static final String NAME = "OminousNote";
    public static final String ID = Deathbringer.makeID(NAME);
    private static final RelicTier RARITY = RelicTier.COMMON;
    private static final LandingSound SOUND = LandingSound.FLAT;

    public OminousNote() {
        super(ID, NAME, deathbringer.character.Deathbringer.Enums.CARD_COLOR, RARITY, SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new OminousNote();
    }
}
