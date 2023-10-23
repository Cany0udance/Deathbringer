package basicmod.cards.skills;

import basemod.BaseMod;
import basemod.abstracts.CustomSavable;
import basemod.interfaces.PostDrawSubscriber;
import basicmod.actions.UpgradePocketAction;
import basicmod.cards.BaseCard;
import basicmod.character.Deathbringer;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import static basemod.BaseMod.logger;

public class Pocket extends BaseCard implements PostDrawSubscriber {
    public static final String ID = makeID("Pocket");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.SKILL,
            CardRarity.BASIC,
            CardTarget.SELF,
            1
    );

    private boolean triggeredDraw = false;

    public Pocket() {
        super(ID, info);
        this.magicNumber = this.baseMagicNumber = 2;  // Initialize magicNumber and baseMagicNumber
        BaseMod.subscribe(this);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.triggeredDraw = true;
        addToBot(new DrawCardAction(p, this.magicNumber));
        addToBot(new DiscardAction(p, p, 1, false));
        this.exhaust = true;
    }

    @Override
    public void receivePostDraw(AbstractCard c) {
        if (this.triggeredDraw && !this.upgraded) {
            if (c.rarity == CardRarity.RARE) {
                addToBot(new UpgradePocketAction(this));
            }
            this.triggeredDraw = false; // Reset the flag
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Pocket();
    }
}