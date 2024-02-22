package deathbringer.cards.skills;


import basemod.BaseMod;
import basemod.interfaces.PostDrawSubscriber;
import deathbringer.actions.ResetFlagAction;
import deathbringer.cards.BaseCard;
import deathbringer.character.Deathbringer;
import deathbringer.util.CardStats;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Arsenal extends BaseCard implements PostDrawSubscriber {
    public static final String ID = makeID("Arsenal");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1
    );

    private static final int BLOCK = 10;
    private static final int UPG_BLOCK = 12;

    private static final int DRAW = 3;
    private static final int UPG_DRAW = 4;

    private boolean triggeredDraw = false;  // Add this flag
    private int shadowCardsDrawn = 0; // Class member variable

    public Arsenal() {
        super(ID, info);
        setBlock(BLOCK, UPG_BLOCK);
        magicNumber = baseMagicNumber = DRAW;
        BaseMod.subscribe(this);
    }

    public void setTriggeredDraw(boolean value) {
        this.triggeredDraw = value;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.triggeredDraw = true;
        addToBot(new DrawCardAction(p, magicNumber));
        addToBot(new ResetFlagAction(this));
    }


    @Override
    public void receivePostDraw(AbstractCard c) {
        if (this.triggeredDraw && c.tags.contains(Deathbringer.Enums.SHADOW)) {
            shadowCardsDrawn++; // Increment only if the drawn card has the SHADOW tag
        }
    }

    public void triggerArsenalDiscard() {
        if (shadowCardsDrawn > 0) {
            addToBot(new DiscardAction(AbstractDungeon.player, AbstractDungeon.player, shadowCardsDrawn, false));
            shadowCardsDrawn = 0; // Reset the counter after discarding
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Arsenal();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPG_BLOCK - BLOCK);
            upgradeMagicNumber(UPG_DRAW - DRAW);
            initializeDescription();
        }
    }
}
