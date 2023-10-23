package basicmod.cards.skills;

import basicmod.actions.DrawRandomShadowCardsAction;
import basicmod.cards.BaseCard;
import basicmod.cards.statuses.Slipup;
import basicmod.character.Deathbringer;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.ShuffleAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static basicmod.Deathbringer.makeID;

public class Imprudence extends BaseCard {
    public static final String ID = makeID("Imprudence");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            AbstractCard.CardTarget.SELF,
            1 // 1-cost
    );

    private static final int DRAW_AMOUNT = 3;
    private static final int UPG_DRAW_AMOUNT = 1; // Draw up to 4 cards when upgraded

    public Imprudence() {
        super(ID, info);
        magicNumber = baseMagicNumber = DRAW_AMOUNT;
        this.cardsToPreview = new Slipup();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        if (AbstractDungeon.player.discardPile.size() > 0) {
            this.addToBot(new EmptyDeckShuffleAction());
            this.addToBot(new ShuffleAction(AbstractDungeon.player.drawPile, false));
        }

        addToBot(new DrawRandomShadowCardsAction(p, magicNumber));

        addToBot(new MakeTempCardInHandAction(new Slipup(), 1));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPG_DRAW_AMOUNT);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Imprudence();
    }
}
