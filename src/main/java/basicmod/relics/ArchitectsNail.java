package basicmod.relics;

import basicmod.Deathbringer;
import basicmod.powers.AddThornsOnAttackPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class ArchitectsNail extends BaseRelic {
    private static final String NAME = "ArchitectsNail";
    public static final String ID = Deathbringer.makeID(NAME);
    private static final RelicTier RARITY = RelicTier.RARE;
    private static final LandingSound SOUND = LandingSound.CLINK;

    public ArchitectsNail() {
        super(ID, NAME, basicmod.character.Deathbringer.Enums.CARD_COLOR, RARITY, SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];  // Ensure the description is in your localization files
    }

    @Override
    public AbstractRelic makeCopy() {
        return new ArchitectsNail();
    }

    @Override
    public void atTurnStart() {
        if (AddThornsOnAttackPower.architectsNailHitCounter >= 1) {
            this.flash();
            this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        }
        // Reset the hit counter for the next turn
        AddThornsOnAttackPower.architectsNailHitCounter = 0;
    }

    @Override
    public void atPreBattle() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new AddThornsOnAttackPower(AbstractDungeon.player)));
    }
}
