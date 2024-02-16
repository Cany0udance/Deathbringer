package basicmod.cards.statuses;

import basicmod.cards.BaseCard;
import basicmod.character.Deathbringer;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

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

        this.tags.add(Deathbringer.Enums.SHADOW);
        this.cardsToPreview = new VoidCard();
        this.isEthereal = true;
        setBackgroundTexture("basicmod/images/character/cardback/shadowskill.png", "basicmod/images/character/cardback/shadowskill_p.png");
        setOrbTexture("basicmod/images/character/cardback/shadowenergyorb.png", "basicmod/images/character/cardback/shadowenergyorb_p.png");
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
