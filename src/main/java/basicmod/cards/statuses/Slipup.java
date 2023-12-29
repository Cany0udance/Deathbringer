package basicmod.cards.statuses;

import basicmod.cards.BaseCard;
import basicmod.character.Deathbringer;
import basicmod.powers.OutburstPower;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static basicmod.Deathbringer.RED_BORDER_GLOW_COLOR;
import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;

public class Slipup extends BaseCard {
    public static final String ID = makeID("Slipup");
    private static final CardStats info = new CardStats(
            CardColor.COLORLESS,
            CardType.STATUS,
            CardRarity.SPECIAL,
            CardTarget.NONE,
            -2 // Extra integer parameter, assuming this is for cost, it's 0 for a Status card
    );

    public Slipup() {
        super(ID, info);

        initializeDescription();

        // Add "Shadow" keyword to this card
        this.keywords.add("shadow");
        this.cardsToPreview = new VoidCard();
        this.isEthereal = true;
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        this.cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];
        return false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // This card is unplayable, so nothing should happen here
    }

    public void triggerShadowplayEffect() {
        // Discard your hand
        addToBot(new MakeTempCardInDrawPileAction(new VoidCard(), 1, true, true));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Slipup();
    }
}
