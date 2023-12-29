package basicmod.cards.skills;

import basemod.BaseMod;
import basemod.abstracts.CustomSavable;
import basemod.interfaces.PostDrawSubscriber;
import basicmod.actions.UpgradePocketAction;
import basicmod.cards.BaseCard;
import basicmod.character.Deathbringer;
import basicmod.util.CardStats;
import com.evacipated.cardcrawl.mod.stslib.patches.FlavorText;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import static basemod.BaseMod.logger;
import static basicmod.cards.FlavorConstants.FLAVOR_BOX_COLOR;
import static basicmod.cards.FlavorConstants.FLAVOR_TEXT_COLOR;

public class Pocket extends BaseCard implements PostDrawSubscriber {
    public static final String ID = makeID("Pocket");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.SKILL,
            CardRarity.BASIC,
            CardTarget.SELF,
            1
    );

    private int triggeredDraw = 0;

    public Pocket() {
        super(ID, info);
        this.magicNumber = this.baseMagicNumber = 2;
        // Set the flavor text only if the card is not upgraded
        if (!upgraded) {
            FlavorText.AbstractCardFlavorFields.boxColor.set(this, FLAVOR_BOX_COLOR);
            FlavorText.AbstractCardFlavorFields.textColor.set(this, FLAVOR_TEXT_COLOR);
        }
        BaseMod.subscribe(this);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.triggeredDraw = this.magicNumber;
        addToBot(new DrawCardAction(p, this.magicNumber));
        addToBot(new DiscardAction(p, p, 1, false));
        this.exhaust = true;
    }

    @Override
    public void receivePostDraw(AbstractCard c) {
        if (this.triggeredDraw > 0 && !this.upgraded) {
            if (c.rarity == CardRarity.RARE) {
                addToBot(new UpgradePocketAction(this));
            }
            this.triggeredDraw--; // Decrement the counter after each card is drawn
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
            // Remove or hide the flavor text
            removeFlavorText();
        }
    }

    private void removeFlavorText() {
        // Implement logic to remove or hide the flavor text
        // For example, set it to an empty string or null
        FlavorText.AbstractCardFlavorFields.boxColor.set(this, null);
        FlavorText.AbstractCardFlavorFields.textColor.set(this, null);
    }

    @Override
    public AbstractCard makeCopy() {
        return new Pocket();
    }
}