package deathbringer.cards.skills;

import deathbringer.cards.BaseCard;
import deathbringer.character.Deathbringer;
import deathbringer.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;

public class GrowingPains extends BaseCard {
    public static final String ID = makeID("GrowingPains");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.NONE,
            -2  // Energy cost
    );

    private static final int DRAW_AMOUNT = 2;
    private static final int UPGRADE_PLUS_DRAW = 1;  // Increase draw amount by 1 when upgraded
    private static final int POISON_AMOUNT = 1;

    public GrowingPains() {
        super(ID, info);
        this.magicNumber = this.baseMagicNumber = DRAW_AMOUNT;
        this.isEthereal = true;  // Card is Ethereal
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        this.cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];
        return false;
    }

    @Override
    public void triggerWhenDrawn() {
        // Draw additional cards
        AbstractPlayer p = AbstractDungeon.player;
        addToBot(new DrawCardAction(p, this.magicNumber));

        // Gain Poison
        addToBot(new ApplyPowerAction(p, p, new PoisonPower(p, p, POISON_AMOUNT), POISON_AMOUNT));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_DRAW);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new GrowingPains();
    }
}
