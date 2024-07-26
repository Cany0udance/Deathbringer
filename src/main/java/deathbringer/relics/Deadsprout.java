package deathbringer.relics;

import com.megacrit.cardcrawl.relics.AbstractRelic;
import deathbringer.Deathbringer;

public class Deadsprout extends BaseRelic {
    private static final String NAME = "Deadsprout";
    public static final String ID = Deathbringer.makeID(NAME);
    private static final RelicTier RARITY = RelicTier.RARE;
    private static final LandingSound SOUND = LandingSound.MAGICAL;

    public Deadsprout() {
        super(ID, NAME, deathbringer.character.Deathbringer.Enums.CARD_COLOR, RARITY, SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Deadsprout();
    }
}