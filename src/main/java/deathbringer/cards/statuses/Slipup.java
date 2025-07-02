package deathbringer.cards.statuses;

import deathbringer.cards.BaseCard;
import deathbringer.character.Deathbringer;
import deathbringer.interfaces.ShadowEffectable;
import deathbringer.util.CardStats;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Slipup extends BaseCard implements ShadowEffectable {
    public static final String ID = makeID("Slipup");
    private static final CardStats info = new CardStats(
            CardColor.COLORLESS,
            CardType.STATUS,
            CardRarity.SPECIAL,
            CardTarget.NONE,
            -2
    );

    public Slipup() {
        super(ID, info);
        initializeDescription();
        this.tags.add(Deathbringer.Enums.SHADOW);
        this.cardsToPreview = new VoidCard();
        this.isEthereal = true;
        setBackgroundTexture("deathbringer/images/character/cardback/shadowskill.png",
                "deathbringer/images/character/cardback/shadowskill_p.png");
        setOrbTexture("deathbringer/images/character/cardback/shadowenergyorb.png",
                "deathbringer/images/character/cardback/shadowenergyorb_p.png");
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        this.cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];
        return false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    @Override
    public void triggerHalfEffect() {
    }

    @Override
    public void triggerShadowplayEffect() {
        addToBot(new MakeTempCardInDrawPileAction(new VoidCard(), 1, true, true));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Slipup();
    }
}