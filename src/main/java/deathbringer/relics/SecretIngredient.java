package deathbringer.relics;

import deathbringer.Deathbringer;
import deathbringer.powers.VirulencePower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class SecretIngredient extends BaseRelic {
    private static final String NAME = "SecretIngredient";
    public static final String ID = Deathbringer.makeID(NAME);
    private static final RelicTier RARITY = RelicTier.COMMON;
    private static final LandingSound SOUND = LandingSound.MAGICAL;

    public SecretIngredient() {
        super(ID, NAME, deathbringer.character.Deathbringer.Enums.CARD_COLOR, RARITY, SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStart() {
        this.flash();
        addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new VirulencePower(AbstractDungeon.player, 2), 2));
        this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    }

    @Override
    public AbstractRelic makeCopy() {
        return new SecretIngredient();
    }
}
